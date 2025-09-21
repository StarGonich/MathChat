package ru.sber.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sber.practice.model.Chat;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query(value = "select * from chats where user_id_min = :userId or user_id_max = :userId", nativeQuery = true)
    List<Chat> findByUserId(Long userId);
}
