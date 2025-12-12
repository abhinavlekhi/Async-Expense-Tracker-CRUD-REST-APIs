package com.example.expensetracker.service;

import com.example.expensetracker.dto.LoginRequestDTO;
import com.example.expensetracker.dto.RegisterRequestDTO;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.UserRepository;
import com.example.expensetracker.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService (UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void registerUser(RegisterRequestDTO registerRequestDTO) {
        User user = new User();
        user.setUserName (registerRequestDTO.getUsername());
        user.setPassword (passwordEncoder.encode(registerRequestDTO.getPassword()));
        userRepository.save(user);
    }

    public String login(LoginRequestDTO dto) {
        User user = userRepository.findByUserName(dto.getUsername()).orElseThrow(() -> new RuntimeException("Invalid Username/password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username / password");
        }
        return jwtUtil.generateToken(user.getId().getMostSignificantBits(), user.getUserName());
    }
}
