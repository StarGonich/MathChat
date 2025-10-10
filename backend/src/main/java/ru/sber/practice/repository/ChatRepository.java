package ru.sber.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sber.practice.model.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query(value = "select * from chats where first_user_id = :userId or second_user_id = :userId", nativeQuery = true)
    List<Chat> findByUserId(Long userId);

    @Query(value = "SELECT CASE WHEN first_user_id = :userId THEN second_user_id ELSE first_user_id END " +
            "FROM chats WHERE id = :chatId AND (first_user_id = :userId OR second_user_id = :userId)",
            nativeQuery = true)
    Optional<Long> findRecipientIdByUserIdAndChatId(Long userId, Long chatId);
}
