package com.taskflow_api.dto;

public record AuthResponse (
        String token,
        String username,
        String email
){}