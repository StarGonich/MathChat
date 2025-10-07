package ru.sber.practice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@Disabled("Peredelat'")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
//
//    @InjectMocks
//    UserService userService;
//    @Mock
//    UserRepository userRepository;
//    @Mock
//    PasswordEncoder passwordEncoder;
//    @Mock
//    UserMapper userMapper;
//
//    @Test
//    void successfulRegistration() {
//        // given
//        SignUpDTO user1 = new SignUpDTO(
//                "Егор",
//                "Мальцев",
//                "1234567@list.ru",
//                "12321"
//        );
//
//        // Создаем User, который вернет маппер
//        User userFromMapper = new User();
//        userFromMapper.setFirstname("Егор");
//        userFromMapper.setLastname("Мальцев");
//        userFromMapper.setEmail("1234567@list.ru");
//        userFromMapper.setPassword("12321"); // пароль до кодирования
//
//        // Создаем User после кодирования пароля
//        User expectedUser = new User();
//        expectedUser.setFirstname("Егор");
//        expectedUser.setLastname("Мальцев");
//        expectedUser.setEmail("1234567@list.ru");
//        expectedUser.setPassword("encodedPassword"); // пароль после кодирования
//
//        // Настраиваем моки
//        when(userMapper.toUser(user1)).thenReturn(userFromMapper);
//        when(userRepository.existsByEmail("1234567@list.ru")).thenReturn(false);
//        when(passwordEncoder.encode("12321")).thenReturn("encodedPassword");
//        when(userRepository.save(userFromMapper)).thenReturn(expectedUser);
//
//        // when
//        User registeredUser = userService.register(user1);
//
//        // then
////        assertTrue(registeredUser);
//        assertEquals("encodedPassword", user1.password());
//        verify(userMapper).toUser(user1); // проверяем, что маппер был вызван
//    }
//
//
//    @Test
//    void userExists() {
//        SignUpDTO user1 = new SignUpDTO(
//                "Егор",
//                "Мальцев",
//                "0570757@list.ru",
//                "12321"
//        );
//
//        // Создаем User, который вернет маппер
//        User userFromMapper = new User();
//        userFromMapper.setFirstname("Егор");
//        userFromMapper.setLastname("Мальцев");
//        userFromMapper.setEmail("1234567@list.ru");
//        userFromMapper.setPassword("12321"); // пароль до кодирования
//
//        when(userMapper.toUser(user1)).thenReturn(userFromMapper);
//        when(userRepository.existsByEmail(userFromMapper.getEmail())).thenReturn(true);
//
////        Boolean tmp = userService.register(user1);
//
////        assertFalse(tmp);
//    }

}
