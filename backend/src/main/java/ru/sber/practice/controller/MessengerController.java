package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.sber.practice.config.MyUserDetails;
import ru.sber.practice.dto.*;
import ru.sber.practice.service.ChatService;
import ru.sber.practice.service.MessageService;

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

    @GetMapping("/search/global/{search}")
    public ResponseEntity<List<GlobalChatDTO>> getAllChats(@PathVariable String search) {
        List<GlobalChatDTO> users = chatService.getGlobalChats(search);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/chat/create/{myUserId}")
    public ResponseEntity<?> createChat(@PathVariable Long myUserId,
                                        @RequestParam(name = "with", required = true) Long anotherUserId) {
        chatService.createChat(myUserId, anotherUserId);
        return new ResponseEntity<>("Чат создан", HttpStatus.CREATED);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<GetMessagesDTO>> getMessagesByChatId(@PathVariable Long chatId) {
        List<GetMessagesDTO> messages = messageService.getMessagesByChatId(chatId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/chat/{chatId}")
    public ResponseEntity<?> sendMessage(@PathVariable Long chatId, @RequestBody SendMessageDTO sendMessageDTO) {
        log.info("Отправка сообщения {}", sendMessageDTO);
        // Лучше всегда возращать то, что создаётся(заносится в БД)
        messageService.sendMessage(chatId, sendMessageDTO);
        log.info("Сообщение сохранено в БД {}", sendMessageDTO);
//        messagingTemplate.convertAndSendToUser(
//                messengerService.getRecipientId(message.getUserId(), message.getChatId()).toString(),
//                "/queue/messages",
//                message
//        );
        return new ResponseEntity<>("Сообщение отправлено", HttpStatus.CREATED);
    }

    @PostMapping("/chat")
    public ResponseEntity<?> createChat(@AuthenticationPrincipal MyUserDetails userDetails, @RequestBody UserDTO userDTO) {
        chatService.createChat(userDetails, userDTO);
        return new ResponseEntity<>("Чат создан", HttpStatus.CREATED);
    }
}
