package ru.sber.practice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sber.practice.model.Message;
import ru.sber.practice.repository.MessageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public List<Message> getMessages(Long chatId) {
        return messageRepository.findByChatId(chatId);
    }
}
