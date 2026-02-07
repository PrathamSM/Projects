package com.smeportal.profileservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "profile_lifecycle") // New table name
public class ProfileLifecycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "profile_id", nullable = false, unique = true)
    @JsonBackReference
    private Profile profile;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column
    private LocalDateTime deletedAt;

    @Column
    private LocalDateTime restoreAt;

    //06/01
    @Column(name = "last_update")
    private String lastUpdate;

    @Column(name ="Done_by")
    private String DoneBy;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;
}
