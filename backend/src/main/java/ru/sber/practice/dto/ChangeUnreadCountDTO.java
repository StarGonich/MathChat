package ru.sber.practice.dto;

public record ChangeUnreadCountDTO(Long userId,
                                   Long newCount) {
}
