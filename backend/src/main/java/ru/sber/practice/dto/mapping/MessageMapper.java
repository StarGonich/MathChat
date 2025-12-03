package ru.sber.practice.dto.mapping;

import ru.sber.practice.dto.GetMessagesDTO;
import ru.sber.practice.dto.UserDTO;
import ru.sber.practice.dto.WebSocketMessageDTO;
import ru.sber.practice.model.Message;
import ru.sber.practice.model.User;

public final class MessageMapper {
    public static GetMessagesDTO toDTO(Message message) {
        return new GetMessagesDTO(
                message.getUserId().getId(),
                message.getMessageText(),
                message.getMessageDate()
        );
    }

    public static WebSocketMessageDTO toWebSocketDTO(Message message) {
        return new WebSocketMessageDTO(
                message.getChatId().getId(),
                message.getUserId().getId(),
                message.getMessageText(),
                message.getMessageDate()
        );
    }
}