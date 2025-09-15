package ru.sber.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sber.practice.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //    @Query(value = "select * from users where email = :email", nativeQuery = true)
    Optional<User> findByEmail(String email);
}

