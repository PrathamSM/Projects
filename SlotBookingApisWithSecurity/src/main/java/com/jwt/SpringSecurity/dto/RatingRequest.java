package com.jwt.SpringSecurity.dto;
import lombok.Data;
@Data
public class RatingRequest {
    private Long bookingid;
    private String comment;
    private Long userid;
}