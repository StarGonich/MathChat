package ru.sber.practice.service.impl;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.minio.errors.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.sber.practice.dto.*;
import ru.sber.practice.dto.mapping.UserMapper;
import ru.sber.practice.model.User;
import ru.sber.practice.repository.UserRepository;
import ru.sber.practice.service.MailSenderService;
import ru.sber.practice.service.S3Service;
import ru.sber.practice.service.UserService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;
    private final S3Service s3Service;

    private User setToken(User user) {
        user.setToken(UUID.randomUUID());
        user.setTokenDate(ZonedDateTime.now());
        user = userRepository.save(user);

        String message = String.format(
                "%s! \n" + "Для подтверждения почты перейдите по ссылке: http://localhost:8080/activate/%s",
                user.getFirstname(),
                user.getToken()
        );

        mailSenderService.send(user.getEmail(), "Activation code", message);

        return user;
    }

    @Override
    public User register(SignUpDTO signUpDTO) {
        log.info("UserService: регистрация SignUpDTO {}", signUpDTO);
        User user = UserMapper.toUser(signUpDTO);

        Optional<User> tmp = userRepository.findByEmail(user.getEmail());

        if (tmp.isPresent()) {
            User userExisted = tmp.get();
            // Если пользователь не активирован, то создаём новый токен для подтверждения почты
            if (userExisted.isEnabled()) {
                return userExisted;
            } else {
                log.info("Перерегистрация пользователя {}", userExisted);
                userExisted.setUsername(user.getUsername());
                userExisted.setFirstname(user.getFirstname());
                userExisted.setLastname(user.getLastname());
                userExisted.setPassword(passwordEncoder.encode(user.getPassword()));
                return setToken(userExisted);
            }
        }
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return setToken(user);
        }
    }

    @Override
    public Page<UserDTO> findAllUsersDTO(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserMapper::toDTO);
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public boolean activateUser(UUID token) {
        Optional<User> userToken = userRepository.findByToken(token);
        if (userToken.isPresent()) {
            User user = userToken.get();
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            ZonedDateTime tokenDateTime = user.getTokenDate().plusMinutes(5);
            // Если срок службы токена истёк, то возвращаем false
            // (возможно стоит переделать, чтобы пользователь понимал, истёк ли у него токен или он неправильный)
            if (zonedDateTime.isAfter(tokenDateTime)) {
                log.info("Срок действия токена активации почты истёк");
                return false;
            }

            user.setToken(null);
            user.setEnabled(true);

            userRepository.save(user);
            log.info("Пользователь {} успешно активирован", user.getEmail());
            return true;
        } else {
            // При отсутствии пользователя с таким токеном возвращаем false
            log.info("Неправильный токен при активации почты");
            return false;
        }
    }

    @Override
    public boolean passwordForgotten(EmailDTO emailDTO) {
        Optional<User> tmp = userRepository.findByEmail(emailDTO.email());
        if (tmp.isPresent()) {
            User user = tmp.get();
            if (user.isEnabled()) {
                log.info("Отправка письма для смены пароля {}", user);

                user.setToken(UUID.randomUUID());
                user.setTokenDate(ZonedDateTime.now());
                userRepository.save(user);

                String message = String.format(
                        "%s! \n" + "Для смены пароля перейдите по ссылке: https://localhost:8080/changePassword/%s",
                        user.getFirstname(),
                        user.getToken()
                );

                mailSenderService.send(user.getEmail(), "Activation code", message);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean changePassword(UUID token, PasswordDTO passwordDTO) {
        Optional<User> userToken = userRepository.findByToken(token);
        if (userToken.isPresent()) {
            User user = userToken.get();
            // Если пользователь не активирован, то возвращаем false
            if (!user.isEnabled()) {
                log.info("Пользователь не активирован");
                return false;
            }

            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            ZonedDateTime tokenDateTime = user.getTokenDate().plusMinutes(5);

            //
            // Если срок службы токена истёк, то возвращаем false
            // (возможно стоит переделать, чтобы пользователь понимал, истёк ли у него токен или он неправильный)
            //
            if (zonedDateTime.isAfter(tokenDateTime)) {
                log.info("Срок действия токена сброса пароля истёк");
                return false;
            }

            user.setPassword(passwordEncoder.encode(passwordDTO.password()));
            user.setToken(null);

            userRepository.save(user);

            return true;
        } else {
            // При отсутствии пользователя с таким токеном возвращаем false
            log.info("Неправильный токен сброса пароля");
            return false;
        }
    }

    @Override
    public User findById(Long id) {
        Optional<User> tmp = userRepository.findById(id);
        return tmp.orElse(null);
    }


    @Override
    public User updateUser(Long userId, UpdatableUserDTO updatableUserDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Нет пользователя с данным Id"));
        user.setUsername(updatableUserDTO.username());
        user.setFirstname(updatableUserDTO.firstname());
        user.setLastname(updatableUserDTO.lastname());
        userRepository.save(user);
        return user;
    }

    public String changeAvatar(Long id, MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Смена аватарки: не найден пользователь с id=" + id));
        String key = s3Service.uploadFile(file);
        user.setImageUrl(key);
        userRepository.save(user);
        return key;
    }

    @Override
    public void updatePassword(Long userId, ChangePasswordDTO passwords) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Нет пользователя с данным Id"));
        log.info("Password in DB: " + user.getPassword());
        log.info("Encoded oldPassword: " + passwordEncoder.encode(passwords.oldPassword()));
        // Правильное сравнение - используем matches()
        if (passwordEncoder.matches(passwords.oldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwords.newPassword()));
            userRepository.save(user);
            log.info("Пароль успешно обновлен");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неправильный старый пароль");
        }
    }

    @Override
    public User blockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Не найден пользователь с id=" + id));

        if (!user.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Пользователь уже заблокирован");
        }

        log.info("Блокировка пользователя: {}", id);
        user.setEnabled(false);
        return userRepository.save(user);
    }

    @Override
    public User unblockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Не найден пользователь с id=" + id));

        if (user.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Пользователь уже активен");
        }

        log.info("Разблокировка пользователя: {}", id);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Override
    public Page<User> searchUsers(String search, Pageable pageable) {
        return userRepository.searchUsers(search, pageable);
    }
}