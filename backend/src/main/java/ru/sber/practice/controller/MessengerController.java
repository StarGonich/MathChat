package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;
import ru.sber.practice.service.MessengerService;

import java.util.List;

@RestController
@RequestMapping("/api/messenger/")
@RequiredArgsConstructor
public class MessengerController {
    private final MessengerService messengerService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Chat>> getChats(@PathVariable Long userId) {
        List<Chat> chats = messengerService.getChats(userId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getMessagesByChatId(@PathVariable Long chatId) {
        List<Message> messages = messengerService.getMessages(chatId);
        return ResponseEntity.ok(messages);
    }
}
