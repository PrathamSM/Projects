package com.assessmentprod.userServiceV1.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    public UserData() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public com.assessmentprod.userServiceV1.entity.role getRole() {
        return role;
    }

    public void setRole(com.assessmentprod.userServiceV1.entity.role role) {
        this.role = role;
    }

    private String email;

    private String password;

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    public role role;      // Enum contains {  USER,ADMIN,HIRING_MANAGER}

    public UserData(long l, String user1, String mail, com.assessmentprod.userServiceV1.entity.role role) {
    }
}
