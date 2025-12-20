package ru.sber.practice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true, name = "username")
    private String username;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(unique = true, name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    // Токен для подтверждения регистрации по почте
    @Column(name = "token")
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
    @Column(name = "provider")
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

}