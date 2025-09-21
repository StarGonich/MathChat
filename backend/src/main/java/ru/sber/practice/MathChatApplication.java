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
    public CommandLineRunner initData(UserService userService) {
        return (args) -> {
            User user1 = new User();
            user1.setFirstname("Егор");
            user1.setLastname("Мальцев");
            user1.setEmail("0570757@list.ru");
            user1.setPassword("12321");
            userService.register(user1);

            User user2 = new User();
            user2.setFirstname("Матвей");
            user2.setLastname("Ковалев");
            user2.setEmail("matveykov@gmail.com");
            user2.setPassword("12321");
            userService.register(user2);

            User user3 = new User();
            user3.setFirstname("Алексей");
            user3.setLastname("Величайший");
            user3.setEmail("alexey8837@gmail.com");
            user3.setPassword("12321");
            userService.register(user3);
        };
    }

}
