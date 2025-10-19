package ru.sber.practice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sber.practice.dto.MessageDTO;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;
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
        Optional<Chat> tmpChat = chatRepository.findById(chatId);

        if (tmpChat.isPresent()) {
            Chat chat = tmpChat.get();
            Message message = new Message();

            message.setUserId(userRepository.findById(messageDTO.userId()).get());
            message.setChatId(chat);
            message.setMessageText(messageDTO.messageText());
            message.setMessageDate(ZonedDateTime.now());
            messageRepository.save(message);

        } else {
            log.info("Чат не найден");
        }
    }
}
