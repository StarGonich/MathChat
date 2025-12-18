package ru.sber.practice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String firstname;

    private String lastname;

    @Column(unique = true)
    private String email;

    private String password;

    // Токен для подтверждения регистрации по почте
    private UUID token;

    @Column(nullable = false, name = "is_enabled")
    @ColumnDefault("false")
    private boolean isEnabled;

    // Дата создания токена
    @Column(name = "token_creation_date")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private ZonedDateTime tokenDate;

    // OAuth2 провайдер (github, google, etc.)
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    // ID пользователя в системе провайдера
    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "image_url")
    private String imageUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "firstUserId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Chat> firstUserChats;

    @JsonIgnore
    @OneToMany(mappedBy = "secondUserId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Chat> secondUserChats;

    @JsonIgnore
    @OneToMany(mappedBy = "userId", fetch = FetchType.EAGER)
    private List<Message> userMessages;

    @Column(nullable = false, name = "is_online")
    @ColumnDefault("false")
    private boolean online;
}