package ru.sber.practice.MathChat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.sber.practice.controller.AuthController;
import ru.sber.practice.model.User;
import ru.sber.practice.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RegisterTest {

    @Mock
    UserService userService;

    @Test
    void regTest1() {
        User user1 = new User();
        user1.setFirstname("Егор");
        user1.setLastname("Мальцев");
        user1.setEmail("1234567@list.ru");
        user1.setPassword("12321");
        userService.register(user1);

        Optional<User> tmp = userService.findByEmail("1234567@list.ru");

        assertEquals(user1, tmp);
    }

}
