package ru.sber.practice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "chats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "first_user_id", nullable = false)
    private User firstUser;

    @ManyToOne
    @JoinColumn(name = "second_user_id", nullable = false)
    private User secondUser;

    @OneToOne
    @JoinColumn(name = "last_message_id")
    private Message lastMessageId;

    @OneToMany(mappedBy = "chatId")
    private List<Message> chatMessages;

//    @Column(nullable = false, name = "first_user_id")
//    private Long firstUser;
//
//    @Column(nullable = false, name = "second_user_id")
//    private Long secondUser;
//
//    @Column(name = "last_message_id")
//    private Long lastMessageId;
}
