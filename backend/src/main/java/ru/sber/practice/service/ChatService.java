package ru.sber.practice.service;

import ru.sber.practice.config.MyUserDetails;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;

import java.util.List;
import java.util.Optional;

public interface ChatService {
    List<Chat> getChats(Long userId);
    void createChat(MyUserDetails userDetails, UserDTO userDTO);
    void createChat(Long userId, UserDTO userDTO);
    Chat getChatById(Long chatId);
    List<UserDTO> getGlobalChats(String search);
    Long getRecipientId(Long userId, Long ChatId);
}
