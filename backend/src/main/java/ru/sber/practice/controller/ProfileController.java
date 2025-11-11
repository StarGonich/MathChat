package ru.sber.practice.controller;

import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sber.practice.dto.ChangePasswordDTO;
import ru.sber.practice.dto.UpdatableUserDTO;
import ru.sber.practice.model.User;
import ru.sber.practice.service.UserService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UpdatableUserDTO updateUserDTO) {
        User user = userService.updateUser(userId, updateUserDTO);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/password/{userId}")
    public ResponseEntity<String> updatePassword(@PathVariable Long userId, @RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.updatePassword(userId, changePasswordDTO);
        return ResponseEntity.ok("Пароль изменён");
    }

    @PutMapping("/change/avatar/{userId}")
    public ResponseEntity<String> changeAvatar(@RequestParam("file") MultipartFile file, @PathVariable Long userId) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try {
            String key = userService.changeAvatar(userId, file);
            return ResponseEntity.ok("Аватарка обновлена: " + key);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка сохранения аватарки");
        }
    }
}
