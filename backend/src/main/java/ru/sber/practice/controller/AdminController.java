package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    // Заблокировать/разблокировать
    @PatchMapping("/block/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> blockUser(@PathVariable Long userId) {
        User user = userService.blockUser(userId);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/unblock/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> unblockUser(@PathVariable Long userId) {
        User user = userService.unblockUser(userId);
        return ResponseEntity.ok(user);
    }
    
}
