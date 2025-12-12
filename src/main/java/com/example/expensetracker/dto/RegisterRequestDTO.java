package com.example.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message="UserName is required")
    private String username;

    @NotBlank(message="Password is required")
    private String password;
}
