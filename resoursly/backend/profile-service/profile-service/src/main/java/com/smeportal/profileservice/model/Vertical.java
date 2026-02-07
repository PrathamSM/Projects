package com.smeportal.profileservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "verticals")
public class Vertical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String verticalName;
}