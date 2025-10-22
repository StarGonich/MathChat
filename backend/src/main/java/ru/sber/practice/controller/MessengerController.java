package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.sber.practice.config.MyUserDetails;
import ru.sber.practice.dto.ContactChatDTO;
import ru.sber.practice.dto.GlobalChatDTO;
import ru.sber.practice.dto.MessageDTO;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.Chat;
import ru.sber.practice.model.Message;
import ru.sber.practice.service.ChatService;
import ru.sber.practice.service.MessageService;
import ru.sber.practice.service.UserService;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessengerController {
    private final ChatService chatService;
    private final MessageService messageService;

    @GetMapping("/search/{userId}")
    public ResponseEntity<List<ContactChatDTO>> getChats(@PathVariable Long userId) {
        List<ContactChatDTO> chats = chatService.getChats(userId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getMessagesByChatId(@PathVariable Long chatId) {
        log.info("Request get /chat/: {}", chatId);
        List<Message> messages = messageService.getMessagesByChatId(chatId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/chat/{chatId}")
    public ResponseEntity<?> sendMessage(@PathVariable Long chatId, @RequestBody MessageDTO messageDTO) {
        log.info("Отправка сообщения {} {}", chatId, messageDTO);
        // Лучше всегда возращать то, что создаётся(заносится в БД)
        messageService.sendMessage(chatId, messageDTO);
        log.info("Сообщение сохранено в БД {}", messageDTO);
//        messagingTemplate.convertAndSendToUser(
//                messengerService.getRecipientId(message.getUserId(), message.getChatId()).toString(),
//                "/queue/messages",
//                message
//        );
        return new ResponseEntity<>("Сообщение отправлено", HttpStatus.CREATED);
    }

    @GetMapping("/exp")
    public ResponseEntity<?> exp() {
        return new ResponseEntity<>("Aboba", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/chat")
    public ResponseEntity<?> createChat(@AuthenticationPrincipal MyUserDetails userDetails, @RequestBody UserDTO userDTO) {
        log.info("Request post /chat: {}", userDTO);
        chatService.createChat(userDetails, userDTO);
        return new ResponseEntity<>("Чат создан", HttpStatus.CREATED);
    }

    @PostMapping("/chat/create/{myUserId}")
    public ResponseEntity<?> createChat(@PathVariable Long myUserId,
                                        @RequestParam(name = "with", required = true) Long anotherUserId) {
        chatService.createChat(myUserId, anotherUserId);
        return new ResponseEntity<>("Чат создан", HttpStatus.CREATED);
    }

    @GetMapping("/search/global/{search}")
    public ResponseEntity<List<GlobalChatDTO>> getAllChats(@PathVariable String search) {
        List<GlobalChatDTO> users = chatService.getGlobalChats(search);
        return ResponseEntity.ok(users);
    }
}
