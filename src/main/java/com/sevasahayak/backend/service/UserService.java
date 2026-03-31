package com.sevasahayak.backend.service;

import com.sevasahayak.backend.dto.UserLoginRequest;
import com.sevasahayak.backend.dto.UserLoginResponse;
import com.sevasahayak.backend.dto.UserRegisterRequest;
import com.sevasahayak.backend.dto.UserResponse;
import com.sevasahayak.backend.entity.User;
import com.sevasahayak.backend.exception.InvalidCredentialsException;
import com.sevasahayak.backend.exception.UserAlreadyExistsException;
import com.sevasahayak.backend.repository.UserRepository;
import com.sevasahayak.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // ==================================================
    // ✅ REGISTER USER
    // ==================================================
    public UserResponse registerUser(UserRegisterRequest request) {

        // 🔴 Duplicate email check
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(
                    "Email already registered: " + request.getEmail()
            );
        }

        // 🔐 Encrypt password + save
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(request.getRole())
                .active(true)
                .build();

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    // ==================================================
    // ✅ LOGIN USER (JWT + ROLE)
    // ==================================================
    public UserLoginResponse login(UserLoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password")
                );

        // 🔐 Password verification
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // 🔑 Generate JWT (EMAIL + ROLE)
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        // 📤 Login Response
        UserLoginResponse response = new UserLoginResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setToken(token);

        return response;
    }

    // ==================================================
    // 🔁 ENTITY → RESPONSE DTO
    // ==================================================
    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());
        response.setActive(user.isActive());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
