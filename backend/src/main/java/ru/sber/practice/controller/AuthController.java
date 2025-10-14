package ru.sber.practice.controller;

import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    private final UserMapper userMapper;

//    @GetMapping("/login/oauth2/")
//    public User welcome(Principal principal) {
//        return userService.findById(Long.parseLong(principal.getName(), 10));
//    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignUpDTO signUpDTO) {
        log.info("AuthController: signUpDTO {}", signUpDTO);
        User registeredUser = userService.register(signUpDTO);
        if (registeredUser.getToken() == null) {
            log.info("Неудачная регистрация");
            return new ResponseEntity<>("Пользователь с данным email уже зарегистрирован!", HttpStatus.BAD_REQUEST);
        }
        UserDTO responseUser = userMapper.toDTO(registeredUser);
        log.info("Регистрация пользователя: {}", responseUser);
        return new ResponseEntity<>(responseUser, HttpStatus.CREATED);
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
    public ResponseEntity<?> changePassword(@PathVariable UUID token, @RequestBody PasswordDTO passwordDTO) {
        boolean passwordChanged = userService.changePassword(token, passwordDTO);
        if (passwordChanged) {
            return new ResponseEntity<>("Пароль изменён", HttpStatus.OK);
        }
        return new ResponseEntity<>("Неправильный токен", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login/oauth2")
    public ResponseEntity<?> oauth2Login(Principal principal, @RequestBody Oauth2LoginDTO oauth2LoginDTO) {
        boolean userLogged = userService.oauth2Login(principal.getName(), oauth2LoginDTO);
        if (userLogged) {
            return new ResponseEntity<>("Пользователь зарегистрирован", HttpStatus.OK);
        }
        return new ResponseEntity<>("Пользователь не найден или уже зарегистрирован", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/login/oauth2")
    public User welcome(Principal principal) {
        return userService.findByProviderId(principal.getName());
    }
}
