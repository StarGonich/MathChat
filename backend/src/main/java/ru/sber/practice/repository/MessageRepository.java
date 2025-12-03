package ru.sber.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sber.practice.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "select * from messages where chat_id = :chatId", nativeQuery = true)
    List<Message> getMessagesByChatId(Long chatId);
}