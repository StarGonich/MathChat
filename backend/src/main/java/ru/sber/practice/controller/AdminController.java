package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sber.practice.model.Message;
import ru.sber.practice.model.User;
import ru.sber.practice.service.MessageService;
import ru.sber.practice.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final MessageService messageService;
    private final UserService userService;

//    @GetMapping("/messages")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public ResponseEntity<List<Message>> findAllMessages() {
//        return ResponseEntity.ok(messageService.findAll());
//    }

    // Заблокировать/разблокировать
    @GetMapping("/block/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> blockUser(@PathVariable Long userId) {
        User user = userService.blockUser(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/unblock/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> unblockUser(@PathVariable Long userId) {
        User user = userService.unblockUser(userId);
        return ResponseEntity.ok(user);
    }
    
}
