package ru.sber.practice.dto.mapping;

import ru.sber.practice.dto.GlobalChatDTO;
import ru.sber.practice.dto.SignUpDTO;
import ru.sber.practice.dto.UpdatableUserDTO;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.model.User;

public final class UserMapper {
    public static User toUser(SignUpDTO signUpDto) {
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

    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail()
        );
    }

    public static GlobalChatDTO toGlobalChatDTO(User user) {
        return new GlobalChatDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getImageUrl()
        );
    }

    public static UpdatableUserDTO toUpdatableUserDTO(User user) {
        return new UpdatableUserDTO(
                user.getUsername(),
                user.getFirstname(),
                user.getLastname()
        );
    }
}
