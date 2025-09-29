package ru.sber.practice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sber.practice.dto.SignUpDTO;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.dto.mapping.UserMapper;
import ru.sber.practice.model.User;
import ru.sber.practice.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    public Boolean register(SignUpDTO signUpDTO) {
        User user = userMapper.toUser(signUpDTO);
        if (userRepository.existsByEmail(user.getEmail())) {
            if (user.isEnabled()) {
                return false;
            } else {
                Optional<User> user1 = userRepository.findByEmail(user.getEmail());
                user1.get().setPassword(passwordEncoder.encode(user.getPassword()));
                user1.get().setToken(UUID.randomUUID().toString());
                userRepository.save(user1.get());

                String message = String.format(
                        "%s! \n" + "Для подтверждения почты перейдите по ссылке: https://localhost:8080/activate/%s",
                        user1.get().getFirstname(),
                        user1.get().getToken()
                );

                mailSender.send(user.getEmail(), "Activation code", message);

                return true;
            }
        }
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setToken(UUID.randomUUID().toString());
            userRepository.save(user);

            String message = String.format(
                    "%s! \n" + "Для подтверждения почты перейдите по ссылке: https://localhost:8080/activate/%s",
                    user.getFirstname(),
                    user.getToken()
            );

            mailSender.send(user.getEmail(), "Activation code", message);

            return true;
        }
    }

    public List<UserDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public boolean activateUser(String token) {
        User user = userRepository.findByToken(token);

        if (user == null) {
            return false;
        }

        user.setToken(null);
        user.setEnabled(true);

        userRepository.save(user);

        return true;
    }
}
