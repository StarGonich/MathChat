package ru.sber.practice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.sber.practice.config.MyUserDetails;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.Chat;
import ru.sber.practice.dto.mapping.UserMapper;
import ru.sber.practice.model.User;
import ru.sber.practice.repository.ChatRepository;
import ru.sber.practice.repository.MessageRepository;
import ru.sber.practice.repository.UserRepository;
import ru.sber.practice.service.ChatService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final MessageRepository messageRepository;
    private final UserMapper userMapper;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    public List<Chat> getChats(Long userId) {
        return chatRepository.findChatByUserId(userId);
    }

    @Override
    public void createChat(MyUserDetails userDetails, UserDTO userDTO) {
        Optional<User> User = userRepository.findByUsername(userDTO.username());
        if (User.isPresent()) {
            Chat chat = new Chat();

            chat.setFirstUserId(userRepository.findByUsername(userDetails.getName()).get());
            chat.setSecondUserId(userRepository.findByUsername(userDTO.username()).get());
            chatRepository.save(chat);
        } else {
            log.info("Пользователь не найден");
        }
    }

    @Override
    public void createChat(Long userId, UserDTO userDTO) {
        Chat chat = new Chat();
        User firstUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Создание чата: не найден пользователь с id=" + userId));
        chat.setFirstUserId(firstUser);
        User secondUser = userRepository.findById(userDTO.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Создание чата: не найден пользователь с id=" + userDTO.id()));
        chat.setSecondUserId(secondUser);
        chat = chatRepository.save(chat);
        log.info("Чат создан {}", chat);
    }

    @Override
    public Chat getChatById(Long chatId) {
        return chatRepository.findById(chatId).orElse(null);
    }

    @Override
    public List<UserDTO> getGlobalChats(String search) {
        return userRepository
                .findBySearchTerm(search)
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public Long getRecipientId(Long userId, Long ChatId) {
        return chatRepository.findRecipientIdByUserIdAndChatId(userId, ChatId)
                .orElseThrow(() -> new RuntimeException("Не можем найти собеседника"));
    }
}
