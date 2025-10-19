package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;
import ru.sber.practice.service.MessengerService;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/messenger/")
@RequiredArgsConstructor
public class MessengerController {
    private final MessengerService messengerService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/search/local/{userId}")
    public ResponseEntity<List<Chat>> getLocalChats(@PathVariable Long userId) {
        List<Chat> chats = messengerService.getLocalChats(userId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/search/global/{search}")
    public ResponseEntity<List<UserDTO>> getAllChats(@PathVariable String search) {
        List<UserDTO> users = messengerService.getGlobalChats(search);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getMessagesByChatId(@PathVariable Long chatId) {
        List<Message> messages = messengerService.getMessages(chatId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        log.info("Отправка сообщения {}", message);
        Message sendedMessage = messengerService.sendMessage(message);
        log.info("Сообщение сохранено в БД {}", message);
        messagingTemplate.convertAndSendToUser(
                messengerService.getRecipientId(message.getUserId(), message.getChatId()).toString(),
                "/queue/messages",
                message
        );
        return new ResponseEntity<>(sendedMessage, HttpStatus.CREATED);
    }

    @PutMapping("/change/avatar/{id}")
    public ResponseEntity<String> changeAvatar(@RequestParam("file") MultipartFile file, @PathVariable Long id) {
        try {
            String key = messengerService.changeAvatar(id, file);
            return ResponseEntity.ok("Аватарка обновлена: " + key);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка сохранения аватарки");
        }
    }
}
