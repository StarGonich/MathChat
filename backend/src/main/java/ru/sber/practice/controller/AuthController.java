package ru.sber.practice.controller;

import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.practice.dto.EmailDTO;
import ru.sber.practice.dto.PasswordDTO;
import ru.sber.practice.dto.SignUpDTO;
import ru.sber.practice.model.User;
import ru.sber.practice.service.MailSenderService;
import ru.sber.practice.service.UserService;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/")
    public String welcome(){
        return "Welcome to the unprotected page";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignUpDTO signUpDTO) {
        boolean registeredUser = userService.register(signUpDTO);
        if (!registeredUser) {
            log.info("Неудачная регистрация");
            return new ResponseEntity<>("Email уже зарегистрирован!", HttpStatus.BAD_REQUEST);
        }
        log.info("Регистрация пользователя: {}", signUpDTO);
        return new ResponseEntity<>(signUpDTO, HttpStatus.CREATED);
    }

    @GetMapping("/activate/{token}")
    public ResponseEntity<?> activate(Model model, @PathVariable UUID token) {
        boolean isActivated = userService.activateUser(token);
        if (isActivated) {
            return new ResponseEntity<>("Почта подтверждена", HttpStatus.OK);
        }
        return new ResponseEntity<>("Неправильный токен", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> forgotPassword(@RequestBody EmailDTO emailDTO) {
        boolean passwordForgotten = userService.passwordForgotten(emailDTO);
        if (passwordForgotten) {
            return new ResponseEntity<>("Письмо для смены пароля отправлено", HttpStatus.OK);
        }
        return new ResponseEntity<>("Почта не найдена или не активирована", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/changePassword/{token}")
    public ResponseEntity<?> changePassword(Model model, @PathVariable UUID token, @RequestBody PasswordDTO passwordDTO) {
        boolean passwordChanged = userService.changePassword(token, passwordDTO);
        if (passwordChanged) {
            return new ResponseEntity<>("Пароль изменён", HttpStatus.OK);
        }
        return new ResponseEntity<>("Неправильный токен", HttpStatus.BAD_REQUEST);
    }
}
