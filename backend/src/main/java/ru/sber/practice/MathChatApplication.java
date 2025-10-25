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
        user.setEmail("test@mail.ru");
        user.setPassword(passwordEncoder.encode("12321"));
        user.setEnabled(true);
        return (args) -> {
            userRepository.save(user);
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
