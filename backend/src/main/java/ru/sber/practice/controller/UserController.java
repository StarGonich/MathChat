package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sber.practice.dto.UpdatableUserDTO;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.User;
import ru.sber.practice.service.UserService;

import java.io.IOException;
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

    @GetMapping("/find/{id}")
    public User findById(@PathVariable Long id) {
        log.info("Request /api/user/find: {}", id);
        return userService.findById(id);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UpdatableUserDTO updateUserDTO) {
        User user = userService.updateUser(userId, updateUserDTO);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/change/avatar/{id}")
    public ResponseEntity<String> changeAvatar(@RequestParam("file") MultipartFile file, @PathVariable Long id) {
        try {
            String key = userService.changeAvatar(id, file);
            return ResponseEntity.ok("Аватарка обновлена: " + key);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка сохранения аватарки");
        }
    }
}
