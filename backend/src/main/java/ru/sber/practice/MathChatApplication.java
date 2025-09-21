package ru.sber.practice;

import io.lettuce.core.protocol.Command;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.sber.practice.model.User;
import ru.sber.practice.service.UserService;

@SpringBootApplication
public class MathChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(MathChatApplication.class, args);
	}

    @Bean
    public CommandLineRunner initUsers(UserService userService, PasswordEncoder passwordEncoder) {
        return (args) -> {
            User user1 = new User();
            user1.setFirstname("Егор");
            user1.setLastname("Мальцев");
            user1.setEmail("0570757@list.ru");
            user1.setPassword("12321");
            userService.register(user1);
        };
    }

}
