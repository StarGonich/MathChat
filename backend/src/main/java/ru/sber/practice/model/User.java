package ru.sber.practice.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
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

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private UUID token;

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
