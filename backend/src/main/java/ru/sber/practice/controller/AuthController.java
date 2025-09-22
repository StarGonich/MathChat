package ru.sber.practice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.sber.practice.model.User;
import ru.sber.practice.service.UserService;

// Настройка CORS
@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class AuthController {
    private final UserService userService;

    /*
        Исправления, благодаря которым сервер запускается
     */
    // Подтягивание компонентов
    @Autowired
    public AuthController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/")
    public String welcome(){
        return "Welcome to the unprotected page";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        if (registeredUser == null) {
            System.out.println("NULL");
            return new ResponseEntity<>("Email уже зарегистрирован!", HttpStatus.BAD_REQUEST);
        }
        System.out.println(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
