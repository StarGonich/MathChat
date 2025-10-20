package ru.sber.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sber.practice.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    //    @Query(value = "select * from users where email = :email", nativeQuery = true)
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByToken(UUID token);
    Optional<User> findByProviderId(String id);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.firstname) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.lastname) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<User> findBySearchTerm(String search);
}

