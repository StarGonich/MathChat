package ru.sber.practice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "user_id_min")
    private Long userIdMin;

    @Column(nullable = false, name = "user_id_max")
    private Long userIdMax;

    @Column(name = "last_message_id")
    private Long lastMessageId;
}
