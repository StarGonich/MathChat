package ru.sber.practice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    /*
        Исправления, благодаря которым сервер запускается
     */
    // При компиляции lombok не создаёт реализации методов (непонятно почему)

    public String getPassword(){
        return password;
    }

    public String getEmail(){
        return email;
    }

    public void setFirstname(String fn){
        firstname = fn;
    }

    public void setLastname(String ln){
        lastname = ln;
    }

    public void setEmail(String em){
        email = em;
    }

    public void setPassword(String pass){
        password = pass;
    }
}