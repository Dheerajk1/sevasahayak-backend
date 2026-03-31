package com.sevasahayak.backend.repository;

import com.sevasahayak.backend.entity.Job;
import com.sevasahayak.backend.entity.JobApplication;
import com.sevasahayak.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    // 🚫 Duplicate apply check
    boolean existsByWorkerAndJob(User worker, Job job);

    // 👨‍💼 Employer → view applicants
    List<JobApplication> findByJob(Job job);

    // 🔐 Employer → update application (safe)
    Optional<JobApplication> findByIdAndJob(Long id, Job job);

    // 👷 Worker → see own applications
    List<JobApplication> findByWorkerEmail(String email);
}
