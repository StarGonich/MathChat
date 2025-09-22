package ru.sber.practice.dto;

import lombok.Data;

@Data
public class SignUpDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
