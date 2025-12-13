package ru.sber.practice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.sber.practice.model.User;
import ru.sber.practice.repository.UserRepository;
import ru.sber.practice.service.UserService;

@SpringBootApplication
public class MathChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(MathChatApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setFirstname("Alice");
        user.setLastname("AAA");
        user.setEmail("aaaaa@mail.ru");
        user.setPassword(passwordEncoder.encode("aaaaa"));
        user.setEnabled(true);

        User user1 = new User();
        user1.setFirstname("Bob");
        user1.setLastname("BBB");
        user1.setEmail("bbbbb@mail.ru");
        user1.setPassword(passwordEncoder.encode("bbbbb"));
        user1.setEnabled(true);
        return (args) -> {
            userRepository.save(user);
            userRepository.save(user1);
        };
    }
}