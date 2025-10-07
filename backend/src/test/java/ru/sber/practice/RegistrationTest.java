package ru.sber.practice;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.sber.practice.dto.mapping.UserMapper;
import ru.sber.practice.repository.UserRepository;
import ru.sber.practice.service.impl.MailSenderServiceImpl;
import ru.sber.practice.service.impl.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Это уже Integration тест, не JUnit

@Disabled("Доделать")
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(AuthController.class)
public class RegistrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockitoBean
    private UserServiceImpl userServiceImpl;
    @MockitoBean
    private UserMapper userMapper;
    @MockitoBean
    private MailSenderServiceImpl mailSenderServiceImpl;
    @MockitoBean
    private UserRepository userRepository;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertInstanceOf(MockServletContext.class, servletContext);
        assertNotNull(webApplicationContext.getBean("authController"));
    }

    @Test
    void registerNewUser_Success() throws Exception {
        String jsonRequest = """
        {
            "firstname": "Алексей",
            "lastname": "Тестов",
            "email": "alexey8837@gmail.com",
            "password": "password123"
        }
        """;

        mockMvc.perform(
                        post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstname").value("Алексей"))
                .andExpect(jsonPath("$.lastname").value("Тестов"))
                .andExpect(jsonPath("$.email").value("alexey8837@gmail.com"));
    }

}
