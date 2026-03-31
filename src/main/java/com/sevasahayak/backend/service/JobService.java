package com.sevasahayak.backend.service;

import com.sevasahayak.backend.entity.Job;
import com.sevasahayak.backend.entity.JobStatus;
import com.sevasahayak.backend.entity.User;
import com.sevasahayak.backend.repository.JobRepository;
import com.sevasahayak.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository,
                      UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    // =================================================
    // 🧑‍💼 Employer creates job
    // =================================================
    public Job createJob(Job job, String email) {

        User employer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Employer not found"));

        job.setEmployer(employer);
        job.setStatus(JobStatus.OPEN);

        return jobRepository.save(job);
    }

    // =================================================
    // 👷 Worker views open jobs (OLD - without pagination)
    // =================================================
    public List<Job> getAllOpenJobs() {
        return jobRepository.findByStatus(JobStatus.OPEN);
    }

    // =================================================
    // 👷 Worker views open jobs (NEW - Paginated)
    // =================================================
    public Page<Job> getAllOpenJobsPaginated(Pageable pageable) {
        return jobRepository.findByStatus(JobStatus.OPEN, pageable);
    }
}
