package ru.sber.practice.service;

import ru.sber.practice.dto.MessageDTO;
import ru.sber.practice.model.Message;

import java.util.List;

public interface MessageService {
    List<Message> getMessagesByChatId(Long chatId);
    void sendMessage(Long chadId, MessageDTO messageDTO);
}
