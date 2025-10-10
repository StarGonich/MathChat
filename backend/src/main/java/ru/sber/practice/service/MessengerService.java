package ru.sber.practice.service;

import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;

import java.util.List;

public interface MessengerService {
    List<Chat> getLocalChats(Long userId);
    List<UserDTO> getGlobalChats(String search);
    List<Message> getMessages(Long chatId);
    Message sendMessage(Message message);
}
