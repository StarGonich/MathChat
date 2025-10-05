package ru.sber.practice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.sber.practice.service.UserService;

@WebMvcTest(UserController.class)
//@Import(SecurityConfig.class) // Не работает, нужно "мокитировать" зависимости из него
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
//    @WithMockUser // Без этого не работает
    // Из интересного, без application.properties и аннотации выше, тест выдаёт 302 (Found)
    public void testFindAllUsers_401() throws Exception {
        mockMvc.perform(get("/api/user/findAll"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser // Без этого не работает
    public void testFindAllUsers_200() throws Exception {
        mockMvc.perform(get("/api/user/findAll"))
                .andExpect(status().isOk());
    }
}