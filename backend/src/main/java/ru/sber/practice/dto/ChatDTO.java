package ru.sber.practice.dto;

import java.time.ZonedDateTime;

// Вся информация дял фронта
public record ChatDTO(Long userId,
                      Long chatId,
                      String firstname,
                      String lastname,
                      String username,
                      String lastMessageText,
                      ZonedDateTime messageDate
                      ) {

}
