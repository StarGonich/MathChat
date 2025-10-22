package ru.sber.practice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.sber.practice.dto.MessageDTO;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;
import ru.sber.practice.model.User;
import ru.sber.practice.repository.ChatRepository;
import ru.sber.practice.repository.MessageRepository;
import ru.sber.practice.repository.UserRepository;
import ru.sber.practice.service.MessageService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    public List<Message> getMessagesByChatId(Long chatId) {
        return messageRepository.getMessagesByChatId(chatId);
    }

    @Override
    public void sendMessage(Long chatId, MessageDTO messageDTO) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Не найден чат с chatId="+chatId));
        Message message = new Message();
        User userAuthor = userRepository.findById(messageDTO.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Не найден пользователь с userId="+messageDTO.userId()));
        message.setUserId(userAuthor);
        message.setChatId(chat);
        message.setMessageText(messageDTO.messageText());
        message.setMessageDate(ZonedDateTime.now());
        message = messageRepository.save(message);
        chat.setLastMessageId(message);
        chatRepository.save(chat);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }
}
