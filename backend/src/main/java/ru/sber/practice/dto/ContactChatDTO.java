package ru.sber.practice.dto;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

// Вся информация для фронта
public record ContactChatDTO(Long userId,
                             Long chatId,
                             String username,
                             String firstname,
                             String lastname,
                             String lastMessageText,
                             OffsetDateTime messageDate,
                             String imageUrl,
                             boolean online,
                             Long unreadCount
) {
}