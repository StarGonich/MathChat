package ru.sber.practice.dto;

import java.time.OffsetDateTime;

//Как и GetMessagesDTO, только плюсом chatId, так как неизвестно, в каком чате его отобразить
public record WebSocketMessageDTO (Long chatId,
                                   Long userId,
                                   String messageText,
                                   OffsetDateTime messageDate){

}