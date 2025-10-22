package ru.sber.practice.service;

import ru.sber.practice.dto.GetMessagesDTO;
import ru.sber.practice.dto.SendMessageDTO;
import ru.sber.practice.model.Message;

import java.util.List;

public interface MessageService {
    List<GetMessagesDTO> getMessagesByChatId(Long chatId);
    void sendMessage(Long chadId, SendMessageDTO sendMessageDTO);

    //Админ запросы
    List<Message> findAll();
}
