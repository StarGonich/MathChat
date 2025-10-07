package ru.sber.practice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;
import ru.sber.practice.dto.mapping.UserMapper;
import ru.sber.practice.repository.ChatRepository;
import ru.sber.practice.repository.MessageRepository;
import ru.sber.practice.repository.UserRepository;
import ru.sber.practice.service.MessengerService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessengerServiceImpl implements MessengerService {
    private final MessageRepository messageRepository;
    private final UserMapper userMapper;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    public List<Chat> getLocalChats(Long userId) {
        return chatRepository.findByUserId(userId);
    }

    @Override
    public List<UserDTO> getGlobalChats(String search) {
        return userRepository
                .findBySearchTerm(search)
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public List<Message> getMessages(Long chatId) {
        return messageRepository.findByChatId(chatId);
    }

    @Override
    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }
}
