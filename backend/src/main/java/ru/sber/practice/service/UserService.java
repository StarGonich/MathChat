package ru.sber.practice.service;

import ru.sber.practice.dto.EmailDTO;
import ru.sber.practice.dto.PasswordDTO;
import ru.sber.practice.dto.SignUpDTO;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User register(SignUpDTO signUpDTO);
    List<UserDTO> findAllUsers();
    User findByEmail(String email);
    boolean activateUser(UUID token);
    boolean passwordForgotten(EmailDTO emailDTO);
    boolean changePassword(UUID token, PasswordDTO passwordDTO);

    User findById(Long id);
}
