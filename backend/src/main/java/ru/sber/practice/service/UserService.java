package ru.sber.practice.service;

import ru.sber.practice.dto.*;
import ru.sber.practice.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User register(SignUpDTO signUpDTO);
    List<UserDTO> findAllUsers();
    User findByEmail(String email);
    User findByToken(UUID token);
    User findByProviderId(String providerId);
    boolean activateUser(UUID token);
    boolean passwordForgotten(EmailDTO emailDTO);
    boolean changePassword(UUID token, PasswordDTO passwordDTO);

    User findById(Long id);

    User updateUser(UserDTO userDTO);
}
