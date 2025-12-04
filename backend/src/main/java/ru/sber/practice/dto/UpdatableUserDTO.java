package ru.sber.practice.dto;

// DTO для редактирования данных о пользователе
// Пароль и почта здесь не относятся, так как их меняет по-особенному, в нашем случае через активацию по почте
public record UpdatableUserDTO(
        String username,
        String firstname,
        String lastname
) {
}