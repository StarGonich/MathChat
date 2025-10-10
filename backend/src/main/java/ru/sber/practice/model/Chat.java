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

    @Column(nullable = false, name = "first_user_id")
    private Long firstUser;

    @Column(nullable = false, name = "second_user_id")
    private Long secondUser;

    @Column(name = "last_message_id")
    private Long lastMessageId;
}
