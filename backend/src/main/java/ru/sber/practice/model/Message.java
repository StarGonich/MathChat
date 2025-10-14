package ru.sber.practice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chatId;

//    @Column(nullable = false, name = "user_id")
//    private Long userId;
//
//    @Column(nullable = false, name = "chat_id")
//    private Long chatId;

    @Column(nullable = false, name = "message_text")
    private String messageText;

    @Column(name = "message_creation_date")
    private ZonedDateTime messageDate;

    @OneToOne(mappedBy = "lastMessageId")
    private Chat chat;
}
