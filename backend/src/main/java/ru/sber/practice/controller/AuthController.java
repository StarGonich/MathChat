package ru.sber.practice.controller;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.sber.practice.config.MyUserDetails;
import ru.sber.practice.dto.*;
import ru.sber.practice.dto.mapping.UserMapper;
import ru.sber.practice.model.User;
import ru.sber.practice.service.MailSenderService;
import ru.sber.practice.service.UserService;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    // при аутентификации через admin ничего не выдаст, но какого-то чёрта у него всё равно будет роль ADMIN
    @GetMapping("/")
    public MyUserDetails test(@AuthenticationPrincipal MyUserDetails userDetails, HttpServletRequest request) {
        log.info("Тест произошёл на /, {}", userDetails);
        log.info("Порт теста - {}", request.getLocalPort());
        return userDetails;
    }

    @GetMapping("/testPort")
    public int testPort(@AuthenticationPrincipal MyUserDetails userDetails, HttpServletRequest request) {
        log.info("Порт теста - {}", request.getLocalPort());
        return request.getLocalPort();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignUpDTO signUpDTO) {
        log.info("AuthController: signUpDTO {}", signUpDTO);
        User registeredUser = userService.register(signUpDTO);
        if (registeredUser.getToken() == null) {
            log.info("Неудачная регистрация");
            return new ResponseEntity<>("Пользователь с данным email уже зарегистрирован!", HttpStatus.BAD_REQUEST); // TODO!!!
        }
        UserDTO responseUser = UserMapper.toDTO(registeredUser);
        log.info("Регистрация пользователя: {}", responseUser);
        return new ResponseEntity<>(responseUser, HttpStatus.CREATED);
    }

    @GetMapping("/activate/{token}")
    public ResponseEntity<?> activate(@PathVariable UUID token) {
        boolean isActivated = userService.activateUser(token);
        if (isActivated) {
            return new ResponseEntity<>("Почта подтверждена", HttpStatus.OK);
        }
        return new ResponseEntity<>("Неправильный токен", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> forgotPassword(@RequestBody EmailDTO emailDTO) {
        log.info("Запрос на смену почты: {}", emailDTO);
        boolean passwordForgotten = userService.passwordForgotten(emailDTO);
        if (passwordForgotten) {
            log.info("Успешный запрос: {}", emailDTO);
            return new ResponseEntity<>("Письмо для смены пароля отправлено", HttpStatus.OK);
        }
        log.info("Неудачный запрос: {}", emailDTO);
        return new ResponseEntity<>("Почта не найдена или не активирована", HttpStatus.BAD_REQUEST); // vue ругается когда встречает BAD_REQUEST
    }

    @PostMapping("/changePassword/{token}")
    public ResponseEntity<?> changePassword(@PathVariable UUID token, @RequestBody PasswordDTO passwordDTO) {
        boolean passwordChanged = userService.changePassword(token, passwordDTO);
        if (passwordChanged) {
            return new ResponseEntity<>("Пароль изменён", HttpStatus.OK);
        }
        return new ResponseEntity<>("Неправильный токен", HttpStatus.BAD_REQUEST);
    }
}
