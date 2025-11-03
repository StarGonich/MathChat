package ru.sber.practice.service;

import ru.sber.practice.config.MyUserDetails;
import ru.sber.practice.dto.ContactChatDTO;
import ru.sber.practice.dto.GlobalChatDTO;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления чатами.
 * Обеспечивает основные операции для сущности Chat.
 */
public interface ChatService {
    /**
     * Метод для находнеия всех чатов пользователя по его id.
     *
     * @param userId id пользователя, у которого ищем чаты.
     * @return список чатов или BAD_REQUEST.
     */
    List<ContactChatDTO> getChats(Long userId);
    /**
     * Метод для создания чата между пользователями.
     *
     * @param firstUserId id первого пользователя.
     * @param secondUserId id второго пользователя.
     */
    void createChat(Long firstUserId, Long secondUserId);
    /**
     * Метод для глобального поиска чатов.
     * Из search ищет все совпадения в нике|имени|фамилии всех пользователей.
     *
     * @param search некоторая строка из символов, по которой находим все совпадения.
     * @return список всех DTO, которые частично или полностью совпали со строкой search.
     */
    List<GlobalChatDTO> getGlobalChats(String search);
    /**
     * Метод для нахождения у пользователя id его собеседника по id чата.
     *
     * @param userId id пользователя, у которого ищем собеседника.
     * @param chatId id чата между пользователем и его собеседником.
     * @return id собеседника.
     */
    Long getRecipientId(Long userId, Long chatId);
}
