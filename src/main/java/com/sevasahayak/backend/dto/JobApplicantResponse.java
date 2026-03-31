package com.sevasahayak.backend.dto;

import com.sevasahayak.backend.entity.ApplicationStatus;

import java.time.LocalDateTime;

public class JobApplicantResponse {

    private Long applicationId;
    private Long workerId;
    private String workerName;
    private String workerEmail;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;

    // ✅ REQUIRED CONSTRUCTOR (FIXES ERROR)
    public JobApplicantResponse(
            Long applicationId,
            Long workerId,
            String workerName,
            String workerEmail,
            ApplicationStatus status,
            LocalDateTime appliedAt
    ) {
        this.applicationId = applicationId;
        this.workerId = workerId;
        this.workerName = workerName;
        this.workerEmail = workerEmail;
        this.status = status;
        this.appliedAt = appliedAt;
    }

    // getters (setter optional)
    public Long getApplicationId() {
        return applicationId;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public String getWorkerEmail() {
        return workerEmail;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }
}
