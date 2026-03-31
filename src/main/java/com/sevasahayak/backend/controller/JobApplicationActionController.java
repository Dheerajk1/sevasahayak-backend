package com.sevasahayak.backend.controller;

import com.sevasahayak.backend.service.JobApplicationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employer/jobs")
public class JobApplicationActionController {

    private final JobApplicationService service;

    public JobApplicationActionController(JobApplicationService service) {
        this.service = service;
    }

    // 👨‍💼 EMPLOYER → ACCEPT / REJECT APPLICATION
    @PreAuthorize("hasRole('EMPLOYER')")
    @PutMapping("/{jobId}/applications/{applicationId}")
    public String updateApplicationStatus(
            @PathVariable Long jobId,
            @PathVariable Long applicationId,
            @RequestParam String action
    ) {
        return service.updateApplicationStatus(jobId, applicationId, action);
    }
}
