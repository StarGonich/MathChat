package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;
import ru.sber.practice.model.User;
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

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        Message sendedMessage = messengerService.sendMessage(message);
        return new ResponseEntity<>(sendedMessage, HttpStatus.CREATED);
    }
}
