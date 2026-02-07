package com.smeprod.authservice.dto;

public record CreateUserReq(String email, String password, String role) {
}
