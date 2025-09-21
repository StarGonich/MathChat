package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sber.practice.model.User;
import ru.sber.practice.service.UserService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/")
    public String welcome(){
        return "Welcome to the unprotected page";
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return new ResponseEntity<>(userService.register(user), HttpStatus.CREATED);
    }
}
