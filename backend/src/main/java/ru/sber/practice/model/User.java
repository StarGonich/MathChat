package ru.sber.practice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.ZonedDateTime;
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

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(unique = true)
    private String email;

    private String password;

    // Токен для подтверждения регистрации по почте
    private UUID token;

    @Column(nullable = false)
//    @ColumnDefault("true")
    private boolean isEnabled;

    // Дата создания токена
    @Column(name = "token_creation_date")
    private ZonedDateTime tokenDate;

    // OAuth2 провайдер (github, google, etc.)
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    // ID пользователя в системе провайдера
    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "image_url")
    private String imageUrl;
}
