package com.assessmentprod.authService.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;


    public role role;      // Enum contains {  USER,ADMIN,HIRING_MANAGER}

}
