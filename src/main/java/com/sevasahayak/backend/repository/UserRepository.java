package com.sevasahayak.backend.repository;

import com.sevasahayak.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // email se user find karne ke liye (registration / login me kaam aayega)
    Optional<User> findByEmail(String email);

    // check karega ki email already exist karti hai ya nahi
    boolean existsByEmail(String email);
}
