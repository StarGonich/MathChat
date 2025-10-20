package ru.sber.practice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.List;

@Entity
@Table(name = "chats", uniqueConstraints={@UniqueConstraint(columnNames = {"first_user_id" , "second_user_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "first_user_id", nullable = false)
    private User firstUserId;

    @ManyToOne
    @JoinColumn(name = "second_user_id", nullable = false)
    private User secondUserId;

    @OneToOne
    @JoinColumn(name = "last_message_id")
    private Message lastMessageId;

    @JsonIgnore
    @OneToMany(mappedBy = "chatId", fetch = FetchType.EAGER)
    private List<Message> chatMessages;
}
