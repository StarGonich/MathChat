package ru.sber.practice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sber.practice.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "select * from message where chat_id = :chatId", nativeQuery = true)
    Page<Message> getMessagesByChatId(Long chatId, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM messages WHERE message_text LIKE '%$ % $%'", nativeQuery = true)
    long getCountOfLaTeXMessages();
}