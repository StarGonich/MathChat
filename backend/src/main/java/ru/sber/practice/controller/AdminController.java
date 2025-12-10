package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<User>> findAllUsers(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20) Pageable pageable) {

        if (search != null && !search.trim().isEmpty()) {
            return ResponseEntity.ok(userService.searchUsers(search.trim(), pageable));
        } else {
            return ResponseEntity.ok(userService.findAllUsers(pageable));
        }
    }

    // Заблокировать/разблокировать
    @PostMapping("/block/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> blockUser(@PathVariable Long userId) {
        User user = userService.blockUser(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/unblock/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> unblockUser(@PathVariable Long userId) {
        User user = userService.unblockUser(userId);
        return ResponseEntity.ok(user);
    }

}