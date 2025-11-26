package ru.sber.practice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.With;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("classpath:/sql/data.sql")
public class MessengerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithUserDetails(value = "12321alexey8837@gmail.com", userDetailsServiceBeanName = "myUserDetailsServiceImpl")
    void getChats_ShouldReturnChatsList_WhenUserExists() throws Exception {
        // Given
        Long userId = 2L;

        // When & Then
        mockMvc.perform(get("/search/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithUserDetails(value = "12321alexey8837@gmail.com", userDetailsServiceBeanName = "myUserDetailsServiceImpl")
    void getChats_ShouldReturnForbidden_WhenUserNotAuthorized() throws Exception {
        // Given
        Long otherUserId = 5L;

        // When & Then
        mockMvc.perform(get("/search/{userId}", otherUserId))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "12321alexey8837@gmail.com", userDetailsServiceBeanName = "myUserDetailsServiceImpl")
    void getGlobalChats_ShouldReturnUsersList_WithSearchTerm() throws Exception {
        // Given
        String searchTerm = "Star";

        // When & Then
        mockMvc.perform(get("/search/global/{searchTerm}", searchTerm))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithUserDetails(value = "12321alexey8837@gmail.com", userDetailsServiceBeanName = "myUserDetailsServiceImpl")
    void getGlobalChats_ShouldReturnEmptyList_WhenNoUsersFound() throws Exception {
        // Given
        String searchTerm = "NonExistentUser123";

        // When & Then
        mockMvc.perform(get("/search/global/{searchTerm}", searchTerm))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithUserDetails(value = "12321alexey8837@gmail.com", userDetailsServiceBeanName = "myUserDetailsServiceImpl")
    void createChat_ShouldCreateChat_WhenUsersExistAndChatDoesNotExist() throws Exception {
        Long firstUserId = 2L;
        Long secondUserId = 5L;

        // When & Then
        mockMvc.perform(post("/chat/create/{firstUserId}", firstUserId)
                        .param("with", secondUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(value = "12321alexey8837@gmail.com", userDetailsServiceBeanName = "myUserDetailsServiceImpl")
    void createChat_ShouldReturnForbidden_WhenUserNotAuthorized() throws Exception {
        Long firstUserId = 5L;
        Long secondUserId = 2L;

        // When & Then
        mockMvc.perform(post("/chat/create/{firstUserId}", firstUserId)
                        .param("with", secondUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "12321alexey8837@gmail.com", userDetailsServiceBeanName = "myUserDetailsServiceImpl")
    void createChat_ShouldCreateChat_WhenFirstUser1IsUser2() throws Exception {
        Long firstUserId = 2L;
        Long secondUserId = 2L;

        // When & Then
        mockMvc.perform(post("/chat/create/{firstUserId}", firstUserId)
                        .param("with", secondUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(value = "12321alexey8837@gmail.com", userDetailsServiceBeanName = "myUserDetailsServiceImpl")
    void createChat_ShouldReturnBadRequest_WhenSecondUserNotFound() throws Exception {
        Long firstUserId = 2L;
        Long secondUserId = 999L;

        // When & Then
        mockMvc.perform(post("/chat/create/{firstUserId}", firstUserId)
                        .param("with", secondUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = "12321alexey8837@gmail.com", userDetailsServiceBeanName = "myUserDetailsServiceImpl")
    void createChat_ShouldReturnBadRequest_WhenUserNotEnabled() throws Exception {
        // Given
        Long firstUserId = 2L;
        Long secondUserId = 3L; // NotActivated user from data.sql

        // When & Then
        mockMvc.perform(post("/chat/create/{firstUserId}", firstUserId)
                        .param("with", secondUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = "12321alexey8837@gmail.com", userDetailsServiceBeanName = "myUserDetailsServiceImpl")
    void getMessages_ShouldReturnMessagesList_WhenChatExists() throws Exception {
        // Given
        Long chatId = 1L;

        // When & Then
        mockMvc.perform(get("/chat/{chatId}?id=2", chatId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithUserDetails(value = "12321alexey8837@gmail.com", userDetailsServiceBeanName = "myUserDetailsServiceImpl")
    void getMessages_ShouldReturnBadRequest_WhenChatNotFound() throws Exception {
        // Given
        Long nonExistentChatId = 999L;

        // When & Then
        mockMvc.perform(get("/chat/{chatId}", nonExistentChatId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = "12321alexey8837@gmail.com", userDetailsServiceBeanName = "myUserDetailsServiceImpl")
    void sendMessage_ShouldSendMessage_WhenValidRequest() throws Exception {
        // Given
        Long chatId = 1L;
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", 2L);
        requestBody.put("messageText", "Test message");

        // When & Then
        mockMvc.perform(post("/chat/{chatId}", chatId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(value = "12321alexey8837@gmail.com", userDetailsServiceBeanName = "myUserDetailsServiceImpl")
    void sendMessage_ShouldReturnBadRequest_WhenChatNotFound() throws Exception {
        // Given
        Long nonExistentChatId = 999L;
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", 2L);
        requestBody.put("messageText", "Test message");

        // When & Then
        mockMvc.perform(post("/chat/{chatId}", nonExistentChatId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = "12321alexey8837@gmail.com", userDetailsServiceBeanName = "myUserDetailsServiceImpl")
    void sendMessage_ShouldReturnBadRequest_WhenUserNotFound() throws Exception {
        // Given
        Long chatId = 1L;
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", 999L);
        requestBody.put("messageText", "Test message");

        // When & Then
        mockMvc.perform(post("/chat/{chatId}", chatId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "egor@gmail.com", userDetailsServiceBeanName = "myUserDetailsServiceImpl")
    void sendMessage_ShouldReturnBadRequest_WhenUserNotInChat() throws Exception {
        // Given
        Long chatId = 1L;
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", 5L); // User who is not in the chat
        requestBody.put("messageText", "Test message");

        // When & Then
        mockMvc.perform(post("/chat/{chatId}", chatId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest());
    }
}