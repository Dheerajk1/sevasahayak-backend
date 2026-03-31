package com.sevasahayak.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleTestController {

    // 👷 WORKER only
    @PreAuthorize("hasRole('WORKER')")
    @GetMapping("/api/worker/test")
    public String workerTest() {
        return "👷 Worker API accessed";
    }

    // 🧑‍💼 EMPLOYER only
    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/api/employer/test")
    public String employerTest() {
        return "🧑‍💼 Employer API accessed";
    }

    // 👑 ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/test")
    public String adminTest() {
        return "👑 Admin API accessed";
    }
}
