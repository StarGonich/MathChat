package ru.sber.practice.MathChat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.sber.practice.model.User;
import ru.sber.practice.service.UserService;

@SpringBootTest
class RegisterTest {
    private final UserService userService;

    RegisterTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    void regTest1() {
        User user1 = new User();
        user1.setFirstname("Егор");
        user1.setLastname("Мальцев");
        user1.setEmail("1234567@list.ru");
        user1.setPassword("12321");
        userService.register(user1);

        System.out.println(userService.findByEmail("1234567@list.ru").isPresent());
    }

}
