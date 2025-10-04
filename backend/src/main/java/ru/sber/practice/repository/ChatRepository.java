package ru.sber.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sber.practice.model.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query(value = "select * from chats where user_id_min = :userId or user_id_max = :userId", nativeQuery = true)
    List<Chat> findByUserId(Long userId);

    @Query("SELECT CASE WHEN c.userIdMin = :userId THEN c.userIdMax ELSE c.userIdMin END " +
            "FROM Chat c WHERE c.id = :chatId AND (c.userIdMin = :userId OR c.userIdMax = :userId)")
    Optional<Long> findRecipientIdByUserIdAndChatId(Long userId, Long chatId);
}
