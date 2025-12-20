package ru.sber.practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import ru.sber.practice.model.User;
import ru.sber.practice.repository.UserRepository;
import ru.sber.practice.service.UserService;

@SpringBootApplication
@EnableScheduling
@EnableRedisHttpSession
public class MathChatApplication implements CommandLineRunner{
  
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(MathChatApplication.class, args);
    }
  
//     @Override
//     public void run(String... args) throws Exception {
//         User user = new User();
//         user.setEmail("test@mail.ru");
//         user.setUsername("Test1");
//         user.setPassword(passwordEncoder.encode("12321"));
//         user.setEnabled(true);
//         userRepository.save(user);

//        User user2 = new User();
//        user2.setEmail("test2@mail.ru");
//        user.setUsername("Test2");
//        user2.setPassword(passwordEncoder.encode("12321"));
//        user2.setEnabled(true);
//        userRepository.save(user2);
//     }
}
