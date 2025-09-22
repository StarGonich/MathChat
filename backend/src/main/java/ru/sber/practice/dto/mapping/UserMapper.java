package ru.sber.practice.dto.mapping;

import org.springframework.stereotype.Service;
import ru.sber.practice.dto.LoginDTO;
import ru.sber.practice.dto.SignUpDTO;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.User;

@Service
public class UserMapper {
    public User toUser(SignUpDTO signUpDTO) {
        return null;
    }

    public User toUser(LoginDTO loginDTO) {
        return null;
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getFirstname(),
                user.getLastname(),
                user.getEmail()
        );
    }
}
