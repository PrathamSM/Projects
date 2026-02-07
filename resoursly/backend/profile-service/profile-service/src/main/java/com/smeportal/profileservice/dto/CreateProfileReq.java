package com.smeportal.profileservice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateProfileReq{

    @NotBlank(message = "Name is required")         //REQ
    private String name;

    @NotNull(message = "Date of Birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Primary Email is required")        //REQ
    @Email(message = "Invalid email format")                //REQ
    @Column(unique = true)
    private String primaryEmail;

    @Email(message = "Invalid email format")
    private String secondaryEmail;

    @NotBlank(message = "Address is required")
    @Size(max = 150, message = "Address cannot exceed 150 characters")
    private String address;

//    @NotBlank(message = "City is required")
    private String city;

//    @NotBlank(message = "State is required")
    private String state;

//    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "PIN Code is required")
    @Size(min = 3, max = 10, message = "PIN code must be between 3 and 10 characters")
    private String pinCode;

    @NotBlank(message = "Contact number is required")
    private String contactNo;
}
