package com.example.expensetracker.service;

import com.example.expensetracker.dto.LoginRequestDTO;
import com.example.expensetracker.dto.RegisterRequestDTO;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.UserRepository;
import com.example.expensetracker.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtil = mock(JwtUtil.class);

        userService = new UserService(userRepository, passwordEncoder, jwtUtil);
    }

    @Test
    void registerUserEncodesPasswordAndSavesUser() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("John");
        dto.setPassword("plain-password");

        when(passwordEncoder.encode("plain-password")).thenReturn("encoded-password");
        userService.registerUser(dto);

        verify(userRepository).save(argThat(user -> "John".equals(user.getUserName()) && "encoded-password".equals(user.getPassword())));
        verify(passwordEncoder).encode("plain-password");
    }

    @Test
    void loginReturnsTokenWhenCredsAreValid() {
        UUID userId = UUID.fromString("12345678-1234-5678-9abc-def012345678");
        User user = new User(userId, "John", "encoded-password");
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("John");
        dto.setPassword("plain-password");

        when(userRepository.findByUserName("John")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plain-password", "encoded-password")).thenReturn(true);
        when(jwtUtil.generateToken(userId.getMostSignificantBits(), "John"))
                .thenReturn("jwt-token");

        assertEquals("jwt-token", userService.login(dto));

        verify(jwtUtil).generateToken(userId.getMostSignificantBits(), "John");
    }
}
