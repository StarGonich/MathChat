package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public Page<UserDTO> findAllUsers(@PageableDefault Pageable pageable) {
        log.info("Request /api/user/findAll.");
        Page<UserDTO> allUsers = userService.findAllUsers(pageable);
        log.info("Response /api/user/findAll: {}", allUsers);
        return allUsers;
    }

    @GetMapping("/find/{id}")
    public User findById(@PathVariable Long id) {
        log.info("Request /api/user/find: {}", id);
        return userService.findById(id);
    }
}
