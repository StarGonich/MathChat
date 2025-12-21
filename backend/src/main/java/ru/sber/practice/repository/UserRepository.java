package ru.sber.practice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    Optional<User> findByIdAndIsEnabledFalse(Long id);
    
   @Query(value = "SELECT * FROM users u WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.firstname) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.lastname) LIKE LOWER(CONCAT('%', :search, '%'))", nativeQuery=true)
    Page<User> searchUsers(@Param("search") String search, Pageable pageable);

    @Query(value = "SELECT * FROM users u WHERE " +
            "(LOWER(u.firstname) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.lastname) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "u.is_enabled = true", nativeQuery = true)
    Page<User> findBySearchTerm(String search, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET is_online = :online " +
            "WHERE id = :id", nativeQuery=true)
    void updateUserStatus(Long id, Boolean online);

    // Для метрик
    long countByIsEnabledTrue();
    long countByOnlineTrue();
}