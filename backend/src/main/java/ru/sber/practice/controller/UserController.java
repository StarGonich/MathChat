package ru.sber.practice.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sber.practice.model.User;
import ru.sber.practice.service.UserService;

import java.util.List;

// Настройка CORS
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    /*
        Исправления, благодаря которым сервер запускается
     */
    // Подтягивание компонентов
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/findAll")
    public List<User> findAllUsers() {
        System.out.println("!!!");
        return userService.findAllUsers();
    }

}
