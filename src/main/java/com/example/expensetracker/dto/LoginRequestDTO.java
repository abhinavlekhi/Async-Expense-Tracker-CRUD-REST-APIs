package com.example.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "userName is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
