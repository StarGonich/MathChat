package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;
import ru.sber.practice.service.ChatService;
import ru.sber.practice.service.MessageService;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessengerController {
    private final ChatService chatService;
    private final MessageService messageService;

    @GetMapping("/search/{userId}")
    public ResponseEntity<List<Chat>> getChats(@PathVariable Long userId) {
        List<Chat> chats = chatService.getChats(userId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getMessagesByChatId(@PathVariable Long chatId) {
        List<Message> messages = messageService.getMessagesByChatId(chatId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/chat/{chatId}")
    public ResponseEntity<?> sendMessage(@PathVariable Long chatId, @RequestBody MessageDTO messageDTO) {
        messageService.sendMessage(chatId, messageDTO);
        return new ResponseEntity<>("Сообщение отправлено",HttpStatus.OK);
    }

    @PostMapping("/chat")
    public ResponseEntity<?> createChat(@AuthenticationPrincipal MyUserDetails userDetails, @RequestBody UserDTO userDTO) {
        chatService.createChat(userDetails, userDTO);
        return new ResponseEntity<>("Чат создан",HttpStatus.OK);
    }

//    @GetMapping("/search/global/{search}")
//    public ResponseEntity<List<UserDTO>> getAllChats(@PathVariable String search) {
//        List<UserDTO> users = messengerService.getGlobalChats(search);
//        return ResponseEntity.ok(users);
//    }
//
//    @PostMapping("/send")
//    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
//        log.info("Отправка сообщения {}", message);
//        Message sendedMessage = messengerService.sendMessage(message);
//        log.info("Сообщение сохранено в БД {}", message);
//        messagingTemplate.convertAndSendToUser(
//                messengerService.getRecipientId(message.getUserId(), message.getChatId()).toString(),
//                "/queue/messages",
//                message
//        );
//        return new ResponseEntity<>(sendedMessage, HttpStatus.CREATED);
//    }

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
