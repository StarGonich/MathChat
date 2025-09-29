package ru.sber.practice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;
import ru.sber.practice.dto.mapping.UserMapper;
import ru.sber.practice.repository.ChatRepository;
import ru.sber.practice.repository.MessageRepository;
import ru.sber.practice.repository.UserRepository;

import java.util.List;

@Service
public class MessengerService {
    private final MessageRepository messageRepository;
    private final UserMapper userMapper;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    /*
        Исправления, благодаря которым сервер запускается
     */
    // Подтягивание компонентов
    @Autowired
    public MessengerService(MessageRepository messageRepository, ChatRepository chatRepository){
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
    }

    public List<Chat> getLocalChats(Long userId) {
        return chatRepository.findByUserId(userId);
    }

    public List<UserDTO> getGlobalChats(String search) {
        return userRepository
                .findBySearchTerm(search)
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public List<Message> getMessages(Long chatId) {
        return messageRepository.findByChatId(chatId);
    }

    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }
}
