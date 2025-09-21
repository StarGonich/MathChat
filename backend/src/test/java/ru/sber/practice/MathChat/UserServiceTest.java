package ru.sber.practice.MathChat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.sber.practice.model.User;
import ru.sber.practice.repository.UserRepository;
import ru.sber.practice.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void successfulRegistration() {
        // given
        User user1 = new User();
        user1.setFirstname("Егор");
        user1.setLastname("Мальцев");
        user1.setEmail("1234567@list.ru");
        user1.setPassword("12321");

        // Настраиваем моки
        when(userRepository.existsByEmail("1234567@list.ru")).thenReturn(false);
        when(passwordEncoder.encode("12321")).thenReturn("encodedPassword");
        when(userRepository.save(user1)).thenReturn(user1);

        User registeredUser = userService.register(user1);

        assertNotNull(registeredUser);
        assertEquals("encodedPassword", user1.getPassword()); // Проверяем кодирование
    }

    @Test
    void userExists() {
        User user1 = new User();
        user1.setFirstname("Егор");
        user1.setLastname("Мальцев");
        user1.setEmail("1234567@list.ru");
        user1.setPassword("12321");

        when(userRepository.existsByEmail(user1.getEmail())).thenReturn(true);

        User tmp = userService.register(user1);

        assertNull(tmp);
    }

}
