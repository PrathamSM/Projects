package com.jwt.SpringSecurity.dto;
import lombok.Data;
@Data

public class BookingRequest {
    private Long userid;
    private Long slotid;
}