package com.example.file_upload1.file_upload1.dto;



import java.time.LocalDate;

public record CreateProfileReq(
        String name,
        LocalDate dateOfBirth,
        String primaryEmail,
        String secondaryEmail,
        String address,
        String city,
        String state,
        String country,
        String pinCode,
        String contactNo
) {}
