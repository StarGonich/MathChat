package ru.sber.practice.dto;

//Сlass или record? так как в материалах и то, и другое часто используется
public record SignUpDTO(String firstname,
                        String lastname,
                        String email,
                        String password) {
}