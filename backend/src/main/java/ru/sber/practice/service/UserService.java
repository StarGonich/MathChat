package ru.sber.practice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sber.practice.dto.SignUpDTO;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.dto.mapping.UserMapper;
import ru.sber.practice.model.User;
import ru.sber.practice.repository.UserRepository;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;

    public Boolean register(SignUpDTO signUpDTO) {
        User user = userMapper.toUser(signUpDTO);
        Optional<User> tmp = userRepository.findByEmail(user.getEmail());
        if (tmp.isPresent()) {
            User userExisted = tmp.get();
            if (userExisted.getToken() == null) {
                return false;
            } else {
                log.info("Перерегистрация пользователя {}", userExisted);
                userExisted.setFirstname(user.getFirstname());
                userExisted.setLastname(user.getLastname());
                userExisted.setPassword(passwordEncoder.encode(user.getPassword()));
                userExisted.setToken(UUID.randomUUID());
                userExisted.setTokenDate(ZonedDateTime.now());
                userRepository.save(userExisted);

                String message = String.format(
                        "%s! \n" + "Для подтверждения почты перейдите по ссылке: https://localhost:8080/activate/%s",
                        userExisted.getFirstname(),
                        userExisted.getToken()
                );

                mailSenderService.send(userExisted.getEmail(), "Activation code", message);

                return true;
            }
        }
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setToken(UUID.randomUUID());
            user.setTokenDate(ZonedDateTime.now());
            userRepository.save(user);

            String message = String.format(
                    "%s! \n" + "Для подтверждения почты перейдите по ссылке: https://localhost:8080/activate/%s",
                    user.getFirstname(),
                    user.getToken()
            );

            mailSenderService.send(user.getEmail(), "Activation code", message);

            return true;
        }
    }

    public List<UserDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public boolean activateUser(UUID token) {
        User user = userRepository.findByToken(token);

        if (user == null) {
            log.info("Неправильный токен");
            return false;
        }

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        ZonedDateTime tokenDateTime = user.getTokenDate().plusMinutes(5);

        if (zonedDateTime.isAfter(tokenDateTime)) {
            log.info("Срок действия токена истёк");
            return false;
        }

        user.setToken(null);

        userRepository.save(user);

        return true;
    }
}
