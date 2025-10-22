package ru.sber.practice.dto;

import java.time.ZonedDateTime;

// де-факто UserDTO, но более урезанный
public record GlobalChatDTO(Long userId,
                            String username,
                            String firstname,
                            String lastname
) {
}
