package com.smeportal.profilestatus.model;


import com.smeportal.profilestatus.converter.JsonConverter;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class ProfileStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long profileId;

    @Column(nullable = false)
    private Boolean ndaSigned = false;

    @Convert(converter = JsonConverter.class) // Availability as JSON
    private Availability availability;

    @Column(nullable = false)
    private Boolean isApproved = false;

}
