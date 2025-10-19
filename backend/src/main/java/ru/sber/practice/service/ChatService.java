package ru.sber.practice.service;

import ru.sber.practice.config.MyUserDetails;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.Chat;

import java.util.List;

public interface ChatService {
    List<Chat> getChats(Long userId);
    void createChat(MyUserDetails userDetails, UserDTO userDTO);
//    Chat getChatByChatId(Long chatId);
//    List<UserDTO> getGlobalChats(String search);
//    List<Message> getMessages(Long chatId);
//    Message sendMessage(Message message);
//    Long getRecipientId(Long userId, Long ChatId);
}
