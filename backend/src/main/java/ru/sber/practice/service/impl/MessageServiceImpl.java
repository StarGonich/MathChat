package ru.sber.practice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.sber.practice.dto.GetMessagesDTO;
import ru.sber.practice.dto.SendMessageDTO;
import ru.sber.practice.dto.WebSocketMessageDTO;
import ru.sber.practice.dto.mapping.MessageMapper;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;
import ru.sber.practice.model.User;
import ru.sber.practice.repository.ChatRepository;
import ru.sber.practice.repository.MessageRepository;
import ru.sber.practice.repository.UserRepository;
import ru.sber.practice.service.MessageService;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    // Открытие чата
    @Override
    public Page<GetMessagesDTO> getMessagesByChatId(Long chatId, Pageable pageable) {
        chatRepository.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Не найден чат с chatId="+chatId));
        return messageRepository.getMessagesByChatId(chatId, pageable)
                .map(MessageMapper::toDTO);
    }

    //Отправка сообщения
    @Override
    public WebSocketMessageDTO sendMessage(Long chatId, SendMessageDTO sendMessageDTO) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Не найден чат с chatId="+chatId));
        Message message = new Message();
        User userAuthor = userRepository.findByIdAndIsEnabledTrue(sendMessageDTO.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Не найден пользователь с userId="+ sendMessageDTO.userId()));
        if (!(userAuthor == chat.getFirstUserId() || userAuthor == chat.getSecondUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "У вас нет чата с данным пользователем");
        }
        message.setUserId(userAuthor);
        message.setChatId(chat);
        message.setMessageText(sendMessageDTO.messageText());
        message.setMessageDate(OffsetDateTime.now());
        message = messageRepository.save(message);
        chat.setLastMessageId(message);
        chat.setUnreadCount(chat.getUnreadCount()+1);
        chatRepository.save(chat);
        return MessageMapper.toWebSocketDTO(message);
    }

    /*
     *
     * ДЛЯ АДМИНА
     * */
    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }
}