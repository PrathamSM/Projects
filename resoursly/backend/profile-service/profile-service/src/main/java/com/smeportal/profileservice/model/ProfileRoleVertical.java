package com.smeportal.profileservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "profile_role_vertical")
public class ProfileRoleVertical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long profileId;

    @Column(nullable = false)
    private Long roleId;

    @Column(nullable = false)
    private Long verticalId;
}