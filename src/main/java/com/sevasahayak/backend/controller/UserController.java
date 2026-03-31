package com.sevasahayak.backend.controller;

import com.sevasahayak.backend.dto.ApiResponse;
import com.sevasahayak.backend.dto.UserRegisterRequest;
import com.sevasahayak.backend.dto.UserLoginRequest;
import com.sevasahayak.backend.dto.UserLoginResponse;
import com.sevasahayak.backend.dto.UserResponse;
import com.sevasahayak.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // =================================================
    // ✅ Test API
    // =================================================
    @GetMapping("/test")
    public ApiResponse<String> test() {
        return ApiResponse.success("User API working fine", "OK");
    }

    // =================================================
    // ✅ Register
    // =================================================
    @PostMapping("/register")
    public ApiResponse<UserResponse> registerUser(@RequestBody UserRegisterRequest request) {

        UserResponse user = userService.registerUser(request);

        return ApiResponse.success("User registered successfully", user);
    }

    // =================================================
    // ✅ Login (JWT)
    // =================================================
    @PostMapping("/login")
    public ApiResponse<UserLoginResponse> login(@RequestBody UserLoginRequest request) {

        UserLoginResponse response = userService.login(request);

        return ApiResponse.success("Login successful", response);
    }
}