package ru.sber.practice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.sber.practice.config.MyUserDetails;
import ru.sber.practice.dto.ContactChatDTO;
import ru.sber.practice.dto.GlobalChatDTO;
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
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    public List<ContactChatDTO> getChats(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Не найден пользователь с id=" + userId));
        return chatRepository.findContactChatsByUserId(userId);
    }

    @Override
    public void createChat(Long firstUserId, Long secondUserId) {
        Chat chat = new Chat();

        User firstUser = userRepository.findById(firstUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Не найден пользователь с id=" + firstUserId));
        chat.setFirstUserId(firstUser);

        User secondUser = userRepository.findById(secondUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Не найден пользователь с id=" + secondUserId));
        chat.setSecondUserId(secondUser);

        if (chatRepository.findChatBy2UserId(firstUserId, secondUserId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Чат между пользователями уже существует");
        }

        chat = chatRepository.save(chat);
        log.info("Чат создан {}", chat);
    }

    //no usages, поэтому и не добавляю обработку Exception
    @Override
    public Chat getChatById(Long chatId) {
        return chatRepository.findById(chatId).orElse(null);
    }

    @Override
    public List<GlobalChatDTO> getGlobalChats(String search) {
        return userRepository
                .findBySearchTerm(search)
                .stream()
                .map(UserMapper::toGlobalChatDTO)
                .toList();
    }

    // Для WebSocket, для отправки по персональному каналу
    public Long getRecipientId(Long userId, Long ChatId) {
        return chatRepository.findRecipientIdByUserIdAndChatId(userId, ChatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Не можем найти собеседника"));
    }
}
