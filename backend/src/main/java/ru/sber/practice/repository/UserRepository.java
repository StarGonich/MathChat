package ru.sber.practice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sber.practice.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndIsEnabledTrue(String username);
    Optional<User> findByToken(UUID token);
    Optional<User> findByProviderId(String id);
    Optional<User> findByIdAndIsEnabledTrue(Long id);

    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.firstname) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.lastname) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "u.isEnabled = true")
    Page<User> findBySearchTerm(String search, Pageable pageable);
}

