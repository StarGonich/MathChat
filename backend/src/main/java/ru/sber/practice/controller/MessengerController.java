package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;
import ru.sber.practice.service.MessengerService;

import java.util.List;

// Настройка CORS
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/messenger/")
public class MessengerController {
    private final MessengerService messengerService;

    /*
        Исправления, благодаря которым сервер запускается
     */
    // Подтягивание компонентов
    @Autowired
    public MessengerController(MessengerService messengerService){
        this.messengerService = messengerService;
    }

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
