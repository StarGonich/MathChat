package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
//    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/search/{userId}")
    public ResponseEntity<List<ContactChatDTO>> getChats(@PathVariable Long userId, @AuthenticationPrincipal MyUserDetails userDetails) {
        //Логика с AuthenticationPrincipal
        if (userDetails.getName().equals(userId.toString())) {
            List<ContactChatDTO> chats = chatService.getChats(userId);
            return ResponseEntity.ok(chats);
        }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/search/global/{search}")
    public ResponseEntity<List<GlobalChatDTO>> getAllChats(@PathVariable String search, @AuthenticationPrincipal MyUserDetails userDetails) {
        if (userDetails.getName() != null) {
            List<GlobalChatDTO> users = chatService.getGlobalChats(search);
            return ResponseEntity.ok(users);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/chat/create/{myUserId}")
    public ResponseEntity<?> createChat(@PathVariable Long myUserId, @AuthenticationPrincipal MyUserDetails userDetails,
                                        @RequestParam(name = "with", required = true) String anotherUserId) {
        log.info("id в строке - {}, id в with - {}", myUserId, anotherUserId);
        if (userDetails.getName().equals(myUserId.toString())) {
            chatService.createChat(myUserId, Long.parseLong(anotherUserId));
            return new ResponseEntity<>("Чат создан", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<GetMessagesDTO>> getMessagesByChatId(@PathVariable Long chatId,
                                                                    @AuthenticationPrincipal MyUserDetails userDetails,
                                                                    @RequestParam(name = "id", required = true) String userId) {
         if (userDetails.getName().equals(userId)) {
            List<GetMessagesDTO> messages = messageService.getMessagesByChatId(chatId);
            return ResponseEntity.ok(messages);
         } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/chat/{chatId}")
    public ResponseEntity<?> sendMessage(@PathVariable Long chatId, @RequestBody SendMessageDTO sendMessageDTO,
                                         @AuthenticationPrincipal MyUserDetails userDetails) {
        if (userDetails.getName().equals(sendMessageDTO.userId().toString())) {
            log.info("Отправка сообщения {}", sendMessageDTO);
            // Лучше всегда возращать то, что создаётся(заносится в БД)
            WebSocketMessageDTO message = messageService.sendMessage(chatId, sendMessageDTO);
            log.info("Сообщение сохранено в БД {}", sendMessageDTO);
//            messagingTemplate.convertAndSendToUser(
//                    chatService.getRecipientId(sendMessageDTO.userId(), chatId).toString(),
//                    "/queue/messages",
//                    message
//            );
            return new ResponseEntity<>("Сообщение отправлено", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
