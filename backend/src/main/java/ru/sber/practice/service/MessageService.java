package ru.sber.practice.service;

import ru.sber.practice.dto.GetMessagesDTO;
import ru.sber.practice.dto.SendMessageDTO;
import ru.sber.practice.dto.WebSocketMessageDTO;
import ru.sber.practice.model.Message;

import java.util.List;

/**
 * Сервис для управления сообщениями.
 * Обеспечивает основные операции для сущности message.
 */
public interface MessageService {
    /**
     * Метод для находнеия всех сообщений чата.
     *
     * @param chatId id чата, у которого ищем сообщения.
     * @return список соообщений или BAD_REQUEST.
     */
    List<GetMessagesDTO> getMessagesByChatId(Long chatId);
    /**
     * Метод для отправки сообщений.
     *
     * @param chatId id чата, в котором отправляем сообщение.
     * @param sendMessageDTO DTO форма, содержит id пользователя и сообщение, которое нужно отправить.
     * @return список соообщений или BAD_REQUEST.
     */
    WebSocketMessageDTO sendMessage(Long chatId, SendMessageDTO sendMessageDTO);

    //Админ запросы
    List<Message> findAll();
}