package com.jwt.SpringSecurity.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jwt.SpringSecurity.model.Slot;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_data") // Best practice to explicitly define table names
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;

    private String username;
    private String email;
    private String password;
    private String expertise;
    private String position;

    // One user can have multiple slots
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Slot> slots;
}