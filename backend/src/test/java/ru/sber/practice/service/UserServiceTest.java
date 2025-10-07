package ru.sber.practice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.sber.practice.dto.SignUpDTO;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.dto.mapping.UserMapper;
import ru.sber.practice.model.User;
import ru.sber.practice.repository.UserRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MailSenderService mailSenderService;

    @InjectMocks
    private UserService userService;

    @Test
    void register_NewUser_Success() {
        // given
        SignUpDTO signUpDTO = new SignUpDTO("John", "Doe", "john@test.com", "password");
        User user = createUser(signUpDTO.firstname(), signUpDTO.lastname(), signUpDTO.email(), signUpDTO.password());
        User savedUser = createUser(user.getFirstname(), user.getLastname(), user.getEmail(), "encodedPassword");
        savedUser.setToken(UUID.randomUUID());
        savedUser.setTokenDate(ZonedDateTime.now());

        when(userMapper.toUser(signUpDTO)).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        // Жалуется, если будет user.getPassword()
        when(passwordEncoder.encode("password")).thenReturn(savedUser.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        User result = userService.register(signUpDTO);

        // then
        assertThat(result).isEqualTo(savedUser);
        verify(userRepository).findByEmail(user.getEmail());
        // Жалуется, если будет user.getPassword()
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
        verify(mailSenderService).send(eq(user.getEmail()), eq("Activation code"), anyString());
    }

    @Test
    void register_ExistingUserWithoutToken_ReturnsExistingUser() {
        // given
        SignUpDTO signUpDTO = new SignUpDTO("John", "Doe", "john@test.com", "password");
        User existingUser = createUser(signUpDTO.firstname(), signUpDTO.lastname(), "john@test.com", "oldPassword");
        existingUser.setToken(null);

        when(userMapper.toUser(signUpDTO)).thenReturn(existingUser);
        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        // when
        User result = userService.register(signUpDTO);

        // then
        assertThat(result).isEqualTo(existingUser);
        verify(userRepository, never()).save(any(User.class));
        verify(mailSenderService, never()).send(anyString(), anyString(), anyString());
    }

    @Test
    void register_ExistingUserWithToken_ReRegistration() {
        // given
        SignUpDTO signUpDTO = new SignUpDTO("John", "NewDoe", "john@test.com", "newPassword");
        User newUser = createUser(signUpDTO.firstname(), signUpDTO.lastname(), signUpDTO.email(), signUpDTO.password());
        User existingUser = createUser("OldJohn", "OldDoe", newUser.getEmail(), "oldPassword");
        existingUser.setToken(UUID.randomUUID());
//        existingUser.setTokenDate(ZonedDateTime.now().minusMinutes(10));

        User updatedUser = createUser(newUser.getFirstname(), newUser.getLastname(), newUser.getEmail(), "encodedNewPassword");
        updatedUser.setToken(UUID.randomUUID());
//        updatedUser.setTokenDate(ZonedDateTime.now());

        when(userMapper.toUser(signUpDTO)).thenReturn(newUser);
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // when
        User result = userService.register(signUpDTO);

        // then
        assertThat(result).isEqualTo(updatedUser);
        verify(userRepository).save(existingUser);
        assertThat(existingUser.getFirstname()).isEqualTo("John");
        assertThat(existingUser.getLastname()).isEqualTo("NewDoe");
        verify(mailSenderService).send(eq("john@test.com"), eq("Activation code"), anyString());
    }

    @Test
    void activateUser_ValidToken_ReturnsTrue() {
        // given
        UUID token = UUID.randomUUID();
        User user = createUser("John", "Doe", "john@test.com", "password");
        user.setToken(token);
        user.setTokenDate(ZonedDateTime.now().minusMinutes(3)); // Токен еще действителен

        when(userRepository.findByToken(token)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        boolean result = userService.activateUser(token);

        // then
        assertThat(result).isTrue();
        assertThat(user.getToken()).isNull();
        verify(userRepository).save(user);
    }

    @Test
    void activateUser_InvalidToken_ReturnsFalse() {
        // given
        UUID token = UUID.randomUUID();
        when(userRepository.findByToken(token)).thenReturn(null);

        // when
        boolean result = userService.activateUser(token);

        // then
        assertThat(result).isFalse();
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void activateUser_ExpiredToken_ReturnsFalse() {
        // given
        UUID token = UUID.randomUUID();
        User user = createUser("John", "Doe", "john@test.com", "password");
        user.setToken(token);
        user.setTokenDate(ZonedDateTime.now().minusMinutes(10)); // Токен просрочен

        when(userRepository.findByToken(token)).thenReturn(user);

        // when
        boolean result = userService.activateUser(token);

        // then
        assertThat(result).isFalse();
        verify(userRepository, never()).save(any(User.class));
    }

    private User createUser(String firstname, String lastname, String email, String password) {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}