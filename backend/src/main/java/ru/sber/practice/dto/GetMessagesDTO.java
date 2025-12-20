package ru.sber.practice.dto;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

//Для фронта, и там уже должен быть определён username, lastname и firstname для этого Id,
//так как чат целенаправленно открывался на конкретного пользователя
public record GetMessagesDTO(
        Long userId,
        String messageText,
        ZonedDateTime messageDate
) {

}