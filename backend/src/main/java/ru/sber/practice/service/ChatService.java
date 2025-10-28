package ru.sber.practice.service;

import ru.sber.practice.config.MyUserDetails;
import ru.sber.practice.dto.ContactChatDTO;
import ru.sber.practice.dto.GlobalChatDTO;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;

import java.util.List;
import java.util.Optional;

public interface ChatService {
    List<ContactChatDTO> getChats(Long userId);
    void createChat(Long firstUserId, Long secondUserId);
    Chat getChatById(Long chatId);
    List<GlobalChatDTO> getGlobalChats(String search);
    Long getRecipientId(Long userId, Long ChatId);
}
