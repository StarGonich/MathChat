package ru.sber.practice.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.sber.practice.model.User;
import ru.sber.practice.repository.UserRepository;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("classpath:/sql/data.sql")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerUserAndActivate_Integration_Success() throws Exception {
        String email = "notalexey8837@gmail.com";

        // Шаг 1: Регистрируем пользователя
        String registerRequest = String.format("""
        {
            "username": "testov228",
            "firstname": "Алексей",
            "lastname": "Тестов",
            "email": "%s",
            "password": "testpassword"
        }
        """, email);

        mockMvc.perform(
                        post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(registerRequest)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstname").value("Алексей"))
                .andExpect(jsonPath("$.lastname").value("Тестов"))
                .andExpect(jsonPath("$.email").value("notalexey8837@gmail.com"));

        User user = userRepository.findByEmail(email).orElseThrow();
        UUID activationToken = user.getToken();
        assertThat(activationToken).isNotNull();
        assertThat(user.isEnabled()).isFalse();

        mockMvc.perform(
                        get("/activate/{token}", activationToken)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Почта подтверждена"));

        User activatedUser = userRepository.findByEmail(email).orElseThrow();
        assertThat(activatedUser.isEnabled()).isTrue();
        assertThat(activatedUser.getToken()).isNull();
    }

    @Test
    void register_ReRegisterInactiveUser_NewTokenAndActivation_Success() throws Exception {
        String email = "reregister@user.com";

        // Шаг 1: Первая регистрация
        String firstRegisterJson = String.format("""
        {
            "username": "ipolit",
            "firstname": "Иполитий",
            "lastname": "Мержевод",
            "email": "%s",
            "password": "password1"
        }
        """, email);

        mockMvc.perform(
                        post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(firstRegisterJson)
                )
                .andExpect(status().isCreated());

        // Получаем первого пользователя
        User firstUser = userRepository.findByEmail(email).orElseThrow();
        UUID firstToken = firstUser.getToken();
        String firstPassword = firstUser.getPassword();

        assertThat(firstUser.isEnabled()).isFalse();
        assertThat(firstUser.getFirstname()).isEqualTo("Иполитий");

        // Шаг 2: Перерегистрация с новыми данными
        String secondRegisterJson = String.format("""
        {
            "username": "ipolit",
            "firstname": "Иполлитий",
            "lastname": "Мержевод",
            "email": "%s",
            "password": "password2"
        }
        """, email);

        mockMvc.perform(
                        post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(secondRegisterJson)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname").value("Иполлитий"))
                .andExpect(jsonPath("$.lastname").value("Мержевод"));

        User secondUser = userRepository.findByEmail(email).orElseThrow();
        UUID secondToken = secondUser.getToken();

        assertThat(secondUser.getFirstname()).isEqualTo("Иполлитий");
        assertThat(secondUser.getLastname()).isEqualTo("Мержевод");
        assertThat(secondUser.getUsername()).isEqualTo(null);

        assertThat(secondToken).isNotEqualTo(firstToken);

        assertThat(secondUser.getPassword()).isNotEqualTo(firstPassword);
        assertThat(secondUser.isEnabled()).isFalse(); // Все еще не активирован

        // Шаг 3: Пытаемся активировать старым токеном - должна быть ошибка
        mockMvc.perform(get("/activate/{token}", firstToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Неправильный токен"));

        // Шаг 4: Активируем новым токеном - должен быть успех
        mockMvc.perform(get("/activate/{token}", secondToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Почта подтверждена"));

        // Проверяем, что пользователь активирован
        User activatedUser = userRepository.findByEmail(email).orElseThrow();
        assertThat(activatedUser.isEnabled()).isTrue();
        assertThat(activatedUser.getToken()).isNull();
    }

    @Test
    void registerUser_TokenExpired_ActivationFails() throws Exception {
        String email = "expired@example.com";

        // Шаг 1: Регистрируем пользователя
        String registerJson = String.format("""
    {
        "username": "expireduser",
        "firstname": "Просроченный",
        "lastname": "Токен",
        "email": "%s",
        "password": "password123"
    }
    """, email);

        mockMvc.perform(
                        post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(registerJson)
                )
                .andExpect(status().isCreated());

        // Получаем пользователя
        User user = userRepository.findByEmail(email).orElseThrow();
        UUID originalToken = user.getToken();
        ZonedDateTime originalTokenDate = user.getTokenDate();

        assertThat(user.isEnabled()).isFalse();

        // Шаг 2: Имитируем прохождение времени (больше 5 минут)
        user.setTokenDate(ZonedDateTime.now().minusMinutes(6)); // Токен просрочен
        userRepository.save(user);
        userRepository.flush();

        // Шаг 3: Пытаемся активировать просроченным токеном - должна быть ошибка
        mockMvc.perform(get("/activate/{token}", originalToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Неправильный токен"));

        // Проверяем, что пользователь остался неактивированным
        User notActivatedUser = userRepository.findByEmail(email).orElseThrow();
        assertThat(notActivatedUser.isEnabled()).isFalse();
        assertThat(notActivatedUser.getToken()).isEqualTo(originalToken); // Токен не очищен при ошибке

        // Шаг 4: Перерегистрируемся чтобы получить новый токен
        String reregisterJson = String.format("""
    {
        "username": "newexpireduser",
        "firstname": "Обновленный",
        "lastname": "Пользователь",
        "email": "%s",
        "password": "newpassword123"
    }
    """, email);

        mockMvc.perform(
                        post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(reregisterJson)
                )
                .andExpect(status().isCreated());

        // Получаем пользователя с новым токеном
        User reregisteredUser = userRepository.findByEmail(email).orElseThrow();
        UUID newToken = reregisteredUser.getToken();

        // Проверяем, что токен обновился
        assertThat(newToken).isNotEqualTo(originalToken);
        assertThat(reregisteredUser.getTokenDate()).isAfter(originalTokenDate);
        assertThat(reregisteredUser.isEnabled()).isFalse();

        // Шаг 5: Активируем новым токеном - должен быть успех
        mockMvc.perform(get("/activate/{token}", newToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Почта подтверждена"));

        // Проверяем активацию
        User finallyActivatedUser = userRepository.findByEmail(email).orElseThrow();
        assertThat(finallyActivatedUser.isEnabled()).isTrue();
        assertThat(finallyActivatedUser.getToken()).isNull();
    }

    //Галиматья

    @Test
    void register_ExistingActiveUser_ReturnsBadRequest() throws Exception {
        String jsonRequest = """
        {
            "username": "existinguser",
            "firstname": "Иван",
            "lastname": "Существующий",
            "email": "12321alexey8837@gmail.com",
            "password": "password123"
        }
        """;

        mockMvc.perform(
                        post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пользователь с данным email уже зарегистрирован!"));
    }

    @Disabled("Нужно добавить в контроллер ответ 'Токен просрочен'")
    @Test
    void activate_ExpiredToken_ReturnsBadRequest() throws Exception {
        // Предполагаем, что в data.sql есть пользователь с просроченным токеном, но пока не проверяется
        UUID expiredToken = UUID.fromString("11111111-1111-1111-1111-111111111111");

        mockMvc.perform(
                        get("/activate/{token}", expiredToken)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Неправильный токен"));
    }

//    @Test
//    void forgotPassword_ValidEmail_ReturnsSuccess() throws Exception {
//        String jsonRequest = """
//        {
//            "email": "12321alexey8837@gmail.com"
//        }
//        """;
//
//        mockMvc.perform(
//                        post("/changePassword")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(jsonRequest)
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().string("Письмо для смены пароля отправлено"));
//    }
//
//    @Test
//    void forgotPassword_NonExistentEmail_ReturnsBadRequest() throws Exception {
//        String jsonRequest = """
//        {
//            "email": "nonexistent@user.com"
//        }
//        """;
//
//        mockMvc.perform(
//                        post("/changePassword")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(jsonRequest)
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Почта не найдена или не активирована"));
//    }
//
//    @Test
//    void forgotPassword_InactiveUser_ReturnsBadRequest() throws Exception {
//        // Предполагаем, что в data.sql есть неактивированный пользователь
//        String jsonRequest = """
//        {
//            "email": "inactive@user.com"
//        }
//        """;
//
//        mockMvc.perform(
//                        post("/changePassword")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(jsonRequest)
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Почта не найдена или не активирована"));
//    }
//
//    @Test
//    void changePassword_ValidToken_ReturnsSuccess() throws Exception {
//        UUID validToken = UUID.fromString("22222222-2222-2222-2222-222222222222");
//
//        String jsonRequest = """
//        {
//            "password": "newPassword123"
//        }
//        """;
//
//        mockMvc.perform(
//                        post("/changePassword/{token}", validToken)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(jsonRequest)
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().string("Пароль изменён"));
//    }
//
//    @Test
//    void changePassword_InvalidToken_ReturnsBadRequest() throws Exception {
//        UUID invalidToken = UUID.fromString("00000000-0000-0000-0000-000000000000");
//
//        String jsonRequest = """
//        {
//            "password": "newPassword123"
//        }
//        """;
//
//        mockMvc.perform(
//                        post("/changePassword/{token}", invalidToken)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(jsonRequest)
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Неправильный токен"));
//    }
//
//    @Test
//    void changePassword_ExpiredToken_ReturnsBadRequest() throws Exception {
//        UUID expiredToken = UUID.fromString("33333333-3333-3333-3333-333333333333");
//
//        String jsonRequest = """
//        {
//            "password": "newPassword123"
//        }
//        """;
//
//        mockMvc.perform(
//                        post("/changePassword/{token}", expiredToken)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(jsonRequest)
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Неправильный токен"));
//    }
}