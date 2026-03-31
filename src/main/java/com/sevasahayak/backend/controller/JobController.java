package com.sevasahayak.backend.controller;

import com.sevasahayak.backend.dto.ApiResponse;
import com.sevasahayak.backend.entity.Job;
import com.sevasahayak.backend.service.JobService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // =================================================
    // 🧑‍💼 EMPLOYER → Create Job
    // =================================================
    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping
    public ApiResponse<Job> createJob(@RequestBody Job job) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Job createdJob = jobService.createJob(job, email);

        return ApiResponse.success("Job created successfully", createdJob);
    }

    // =================================================
    // 👷 WORKER → View Open Jobs (Paginated + Sorting)
    // =================================================
    @PreAuthorize("hasRole('WORKER')")
    @GetMapping
    public ApiResponse<Page<Job>> viewOpenJobs(Pageable pageable) {

        Page<Job> jobs = jobService.getAllOpenJobsPaginated(pageable);

        return ApiResponse.success("Jobs fetched successfully", jobs);
    }

    // =================================================
    // 🌍 PUBLIC → View Open Jobs (No Login Required)
    // =================================================
    @GetMapping("/public")
    public ApiResponse<Page<Job>> viewPublicOpenJobs(Pageable pageable) {

        Page<Job> jobs = jobService.getAllOpenJobsPaginated(pageable);

        return ApiResponse.success("Jobs fetched successfully", jobs);
    }
}