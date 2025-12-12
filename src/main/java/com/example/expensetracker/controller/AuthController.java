package com.example.expensetracker.controller;


import com.example.expensetracker.dto.ApiResponse;
import com.example.expensetracker.dto.LoginRequestDTO;
import com.example.expensetracker.dto.RegisterRequestDTO;
import com.example.expensetracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController (UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        userService.registerUser(registerRequestDTO);
        return ResponseEntity.ok(new ApiResponse<>("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequestDTO dto) {
        String token = userService.login(dto);
        return ResponseEntity.ok(new ApiResponse<>("Login successful", token));
    }
}
