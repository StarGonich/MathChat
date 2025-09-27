package ru.sber.practice;

import io.lettuce.core.protocol.Command;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.sber.practice.dto.SignUpDTO;
import ru.sber.practice.dto.UserDTO;
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
            SignUpDTO user1 = new SignUpDTO(
                    "Егор",
                    "Мальцев",
                    "0570757@list.ru",
                    "12321"
            );
            userService.register(user1);

            SignUpDTO user2 = new SignUpDTO(
                    "Матвей",
                    "Ковалев",
                    "matveykov11@gmail.com",
                    "12321"
            );
            userService.register(user2);

            SignUpDTO user3 = new SignUpDTO(
                    "Алексей",
                    "Величайший",
                    "stargonich@gmail.com",
                    "12321"
            );
            userService.register(user3);
        };
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry corsRegistry) {
//                corsRegistry.addMapping("/**").allowedOrigins("http://localhost:9000");
//            }
//        };
//    }

}
