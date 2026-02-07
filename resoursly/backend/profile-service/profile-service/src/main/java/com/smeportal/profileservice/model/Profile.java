package com.smeportal.profileservice.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
@Entity
@Data
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate dateOfBirth;

    @Column(unique = true)
    private String primaryEmail;

    private String secondaryEmail;

    @Size(max = 150, message = "Address cannot exceed 150 characters")
    private String address;

    private String city;

    private String state;

    private String country;

    private String pinCode;

    private String contactNo;


    //26/12
    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private ProfileLifecycle profileLifecycle;
}
