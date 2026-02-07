package com.smeportal.assignresourceservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assign_resource")
public class AssignResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long profileId;

    private String projectId;

    @Column(name = "client_name", nullable = true)
    private String clientName;

    @Column(name = "assigned", nullable = false)
    private boolean assigned = false;

    @Column(name = "projectName", nullable = true)
    private String projectName;

    @Column(name = "dedicated", nullable = false)
    private boolean dedicated = false;
}
