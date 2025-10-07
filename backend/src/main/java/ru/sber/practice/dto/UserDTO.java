package ru.sber.practice.dto;

//Class или record? так как в материалах и то, и другое часто используется
public record UserDTO(Long id,
                      String firstname,
                      String lastname,
                      String email) {
}


