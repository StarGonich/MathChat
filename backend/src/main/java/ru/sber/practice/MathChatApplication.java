package ru.sber.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MathChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(MathChatApplication.class, args);
	}

//    @Bean
//    public CommandLineRunner initData(UserService userService) {
//        return (args) -> {
//            SignUpDTO user1 = new SignUpDTO(
//                    "Егор",
//                    "Мальцев",
//                    "0570757@list.ru",
//                    "12321",
//                    true
//            );
//            userService.register(user1);
//
//            SignUpDTO user2 = new SignUpDTO(
//                    "Матвей",
//                    "Ковалев",
//                    "matveykov11@gmail.com",
//                    "12321",
//                    true
//            );
//            userService.register(user2);
//
//            SignUpDTO user3 = new SignUpDTO(
//                    "Алексей",
//                    "Величайший",
//                    "stargonich@gmail.com",
//                    "12321",
//                    true
//            );
//            userService.register(user3);
//        };
//    }

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
