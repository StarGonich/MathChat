package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sber.practice.dto.UserDTO;
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
        log.info("Запрос всех пользователей");
        return userService.findAllUsers();
    }

}
