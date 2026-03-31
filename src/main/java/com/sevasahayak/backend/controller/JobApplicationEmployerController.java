package com.sevasahayak.backend.controller;

import com.sevasahayak.backend.dto.ApiResponse;
import com.sevasahayak.backend.dto.JobApplicantResponse;
import com.sevasahayak.backend.service.JobApplicationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employer/applications")
@PreAuthorize("hasRole('EMPLOYER')")
public class JobApplicationEmployerController {

    private final JobApplicationService service;

    public JobApplicationEmployerController(JobApplicationService service) {
        this.service = service;
    }

    // =================================================
    // 👀 View applicants
    // =================================================
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
    // ✅ ACCEPT / ❌ REJECT
    // =================================================
    @PutMapping("/{jobId}/{applicationId}")
    public ApiResponse<String> updateStatus(
            @PathVariable Long jobId,
            @PathVariable Long applicationId,
            @RequestParam String action
    ) {
        String result = service.updateApplicationStatus(
                jobId,
                applicationId,
                action
        );

        return ApiResponse.success("Application updated successfully", result);
    }
}