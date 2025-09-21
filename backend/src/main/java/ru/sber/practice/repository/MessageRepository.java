package ru.sber.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sber.practice.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatId(Long chatId);
}
