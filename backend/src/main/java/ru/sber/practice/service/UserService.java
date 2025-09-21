package ru.sber.practice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sber.practice.config.MyUserDetails;
import ru.sber.practice.model.User;
import ru.sber.practice.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }
}
