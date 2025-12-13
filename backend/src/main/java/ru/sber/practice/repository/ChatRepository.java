package ru.sber.practice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.sber.practice.dto.ContactChatDTO;
import ru.sber.practice.model.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    //    @Query(value = "SELECT * from chats where first_user_id = :userId or second_user_id = :userId", nativeQuery = true)
//    List<Chat> findChatByUserId(Long userId);
    @Query(value = """
    SELECT\s
        u.id as userId,
        c.id as chatId,
        u.username as username,
        u.firstname as firstname,
        u.lastname as lastname,
        m.message_text as lastMessageText,
        m.message_creation_date as messageDate,
        u.image_url as imageUrl,
        u.is_online as isOnline,
        c.unread_count as unreadCount
    FROM chats c
    JOIN users u ON u.id = CASE\s
        WHEN c.first_user_id = :userId THEN c.second_user_id\s
        ELSE c.first_user_id\s
    END
    LEFT JOIN messages m ON m.id = c.last_message_id
    WHERE c.first_user_id = :userId OR c.second_user_id = :userId""", nativeQuery = true)
    Page<ContactChatDTO> findContactChatsByUserId(Long userId, Pageable pageable);

    @Query(value = "SELECT CASE WHEN first_user_id = :userId THEN second_user_id ELSE first_user_id END " +
            "FROM chats WHERE id = :chatId AND (first_user_id = :userId OR second_user_id = :userId)",
            nativeQuery = true)
    Optional<Long> findRecipientIdByUserIdAndChatId(Long userId, Long chatId);

    @Query(value = """
    SELECT * FROM chats
    WHERE (first_user_id = :firstUserId AND second_user_id = :secondUserId)
    OR (second_user_id = :firstUserId AND first_user_id = :secondUserId)
    """, nativeQuery = true)
    Optional<Chat> findChatBy2UserId(Long firstUserId, Long secondUserId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Chat SET unreadCount = :unreadCount " +
            "WHERE id = :chatId")
    void updateCount(Long chatId, Long unreadCount);
}