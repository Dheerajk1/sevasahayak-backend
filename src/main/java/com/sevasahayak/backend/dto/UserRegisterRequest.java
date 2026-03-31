package com.sevasahayak.backend.dto;

import com.sevasahayak.backend.entity.Role;
import lombok.Data;

@Data
public class UserRegisterRequest {

    private String name;
    private String email;
    private String password;
    private String phone;
    private Role role;
}
