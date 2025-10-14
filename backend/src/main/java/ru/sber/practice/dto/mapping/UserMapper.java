package ru.sber.practice.dto.mapping;

import org.springframework.stereotype.Service;
import ru.sber.practice.dto.LoginDTO;
import ru.sber.practice.dto.SignUpDTO;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.User;

@Service
public class UserMapper {
    public User toUser(SignUpDTO signUpDto) {
        User user = new User();
        user.setUsername(signUpDto.username());
        user.setFirstname(signUpDto.firstname());
        user.setLastname(signUpDto.lastname());
        user.setEmail(signUpDto.email());
        user.setPassword(signUpDto.password());
        user.setToken(null);
        user.setEnabled(false);
        return user;
    }

    // Вот это мб вообще не пригодится
    public User toUser(LoginDTO loginDTO) {
        return null;
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail()
        );
    }
}
