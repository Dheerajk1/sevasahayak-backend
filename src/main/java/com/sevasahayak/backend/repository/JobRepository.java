package com.sevasahayak.backend.repository;

import com.sevasahayak.backend.entity.Job;
import com.sevasahayak.backend.entity.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    // 🟢 All OPEN jobs (for workers - old method)
    List<Job> findByStatus(JobStatus status);

    // 🟢 All OPEN jobs with pagination & sorting (NEW)
    Page<Job> findByStatus(JobStatus status, Pageable pageable);

    // 🧑‍💼 Jobs posted by employer
    List<Job> findByEmployerId(Long employerId);

    // 🔐 CRITICAL: Job ownership validation
    Optional<Job> findByIdAndEmployerEmail(Long id, String email);
}
