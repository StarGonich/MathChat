package ru.sber.practice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
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
    public Page<ContactChatDTO> getChats(Long userId, Pageable pageable) {
        return chatRepository.findContactChatsByUserId(userId, pageable);
    }

    @Override
    public void createChat(Long firstUserId, Long secondUserId) {
        Chat chat = new Chat();

        User firstUser = userRepository.findByIdAndIsEnabledTrue(firstUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Не найден пользователь с id=" + firstUserId));
        chat.setFirstUserId(firstUser);

        User secondUser = userRepository.findByIdAndIsEnabledTrue(secondUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Не найден пользователь с id=" + secondUserId));
        chat.setSecondUserId(secondUser);

        if (chatRepository.findChatBy2UserId(firstUserId, secondUserId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Чат между пользователями уже существует");
        }

        chat = chatRepository.save(chat);
        log.info("Чат создан {}", chat);
        log.info("Первый пользователь {}, {}, {}", firstUser.getUsername(), firstUser.getFirstname(), firstUser.getLastname());
        log.info("Второй пользователь {}, {}, {}", secondUser.getUsername(), secondUser.getFirstname(), secondUser.getLastname());
    }

    @Override
    public Page<GlobalChatDTO> getGlobalChats(String search, Pageable pageable) {
        return userRepository.findBySearchTerm(search, pageable)
                .map(UserMapper::toGlobalChatDTO);
    }

    // Для WebSocket, для отправки по персональному каналу
    public Long getRecipientId(Long userId, Long ChatId) {
        return chatRepository.findRecipientIdByUserIdAndChatId(userId, ChatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Не можем найти собеседника"));
    }
}
