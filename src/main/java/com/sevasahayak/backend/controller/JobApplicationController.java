package com.sevasahayak.backend.controller;

import com.sevasahayak.backend.dto.ApiResponse;
import com.sevasahayak.backend.dto.JobApplicantResponse;
import com.sevasahayak.backend.service.JobApplicationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService service;

    public JobApplicationController(JobApplicationService service) {
        this.service = service;
    }

    // =================================================
    // 👷 WORKER → Apply for job
    // =================================================
    @PreAuthorize("hasRole('WORKER')")
    @PostMapping("/{jobId}")
    public ApiResponse<String> apply(
            @PathVariable Long jobId,
            Authentication authentication
    ) {
        String result = service.applyForJob(jobId, authentication.getName());
        return ApiResponse.success("Applied successfully", result);
    }

    // =================================================
    // 👨‍💼 EMPLOYER → View applicants
    // =================================================
    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/job/{jobId}")
    public ApiResponse<List<JobApplicantResponse>> viewApplicants(
            @PathVariable Long jobId,
            Authentication authentication
    ) {
        List<JobApplicantResponse> applicants =
                service.getApplicantsForJob(jobId, authentication.getName());

        return ApiResponse.success("Applicants fetched successfully", applicants);
    }

    // =================================================
    // 👷 WORKER → View My Applications
    // =================================================
    @PreAuthorize("hasRole('WORKER')")
    @GetMapping("/my")
    public ApiResponse<List<JobApplicantResponse>> myApplications(
            Authentication authentication
    ) {
        List<JobApplicantResponse> myApps =
                service.getMyApplications(authentication.getName());

        return ApiResponse.success("My applications fetched successfully", myApps);
    }
}