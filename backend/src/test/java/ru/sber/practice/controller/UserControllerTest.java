package ru.sber.practice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.sber.practice.service.UserService;
import ru.sber.practice.service.impl.UserServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("classpath:/sql/data.sql")
//@WebMvcTest(UserController.class)
//@Import(SecurityConfig.class) // Не работает, нужно "мокитировать" зависимости из него
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAllUsers_200() throws Exception {
        var requestBuilder = get("/api/user/findAll");
        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                    {
                                        "id": 1,
                                        "username": "StarGonich",
                                        "firstname": "Алексей",
                                        "lastname": "Величайший",
                                        "email": "12321alexey8837@gmail.com"
                                    }
                                ]
                                """)
                );
    }
}