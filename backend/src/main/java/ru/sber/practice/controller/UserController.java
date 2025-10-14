package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.User;
import ru.sber.practice.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/findAll")
    public List<UserDTO> findAllUsers() {
        log.info("Request /api/user/findAll.");
        List<UserDTO> allUsers = userService.findAllUsers();
        log.info("Response /api/user/findAll: {}", allUsers);
        return allUsers;
    }

    @PostMapping("/find")
    public User findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody UserDTO userDTO) {
        User user = userService.updateUser(userDTO);
        return ResponseEntity.ok(user);
    }
}
