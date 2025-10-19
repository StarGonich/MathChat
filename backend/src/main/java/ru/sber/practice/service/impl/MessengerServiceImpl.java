package ru.sber.practice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;
import ru.sber.practice.dto.mapping.UserMapper;
import ru.sber.practice.model.User;
import ru.sber.practice.repository.ChatRepository;
import ru.sber.practice.repository.MessageRepository;
import ru.sber.practice.repository.UserRepository;
import ru.sber.practice.service.MessengerService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessengerServiceImpl implements MessengerService {
    private final MessageRepository messageRepository;
    private final UserMapper userMapper;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final S3ServiceImpl S3Service;

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

    public Long getRecipientId(Long userId, Long ChatId) {
        return chatRepository.findRecipientIdByUserIdAndChatId(userId, ChatId)
                .orElseThrow(() -> new RuntimeException("Не можем найти собеседника"));
    }

    public String changeAvatar(Long id, MultipartFile file) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Смена аватарки: не найден пользователь с id=" + id));
        String key = S3Service.uploadFile(file);
        user.setImageUrl(key);
        userRepository.save(user);
        return key;
    }
}
