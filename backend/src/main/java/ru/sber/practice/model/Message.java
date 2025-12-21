package ru.sber.practice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "messages", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chatId;

    @Column(nullable = false, name = "message_text")
    private String messageText;

    @Column(name = "message_creation_date")
//    @ColumnDefault("CURRENT_TIMESTAMP")
    private OffsetDateTime messageDate;

    @JsonIgnore
    @OneToOne(mappedBy = "lastMessageId")
    private Chat chat;
}