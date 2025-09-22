package ru.sber.practice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;
import ru.sber.practice.repository.ChatRepository;
import ru.sber.practice.repository.MessageRepository;

import java.util.List;

@Service
public class MessengerService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    /*
        Исправления, благодаря которым сервер запускается
     */
    // Подтягивание компонентов
    @Autowired
    public MessengerService(MessageRepository messageRepository, ChatRepository chatRepository){
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
    }

    public List<Chat> getChats(Long userId) {
        return chatRepository.findByUserId(userId);
    }

    public List<Message> getMessages(Long chatId) {
        return messageRepository.findByChatId(chatId);
    }
}
