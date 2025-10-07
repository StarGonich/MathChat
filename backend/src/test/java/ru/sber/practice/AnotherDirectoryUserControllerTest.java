package ru.sber.practice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.sber.practice.controller.UserController;
import ru.sber.practice.service.impl.UserServiceImpl;

@WebMvcTest(UserController.class)
//@Import(SecurityConfig.class) // Не работает, нужно "мокитировать" зависимости из него
class AnotherDirectoryUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceImpl userServiceImpl;

    /*
    Из интересного:
    Если в application.properties закомментировать всё что связано с OAuth Github,
    то тест выдаёт 401 (Unauthorized), но при этом MathChatApplicationTests не проходят.
    Получается палка о двух концах
    */
    @Test
    public void testFindAllUsers_302() throws Exception {
        mockMvc.perform(get("/api/user/findAll"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser // Без этого не работает
    public void testFindAllUsers_200() throws Exception {
        mockMvc.perform(get("/api/user/findAll"))
                .andExpect(status().isOk());
    }
}