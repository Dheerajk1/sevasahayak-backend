package com.sevasahayak.backend.service;

import com.sevasahayak.backend.dto.JobApplicantResponse;
import com.sevasahayak.backend.entity.*;
import com.sevasahayak.backend.exception.AlreadyAppliedException;
import com.sevasahayak.backend.repository.JobApplicationRepository;
import com.sevasahayak.backend.repository.JobRepository;
import com.sevasahayak.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobApplicationService(
            JobApplicationRepository applicationRepository,
            JobRepository jobRepository,
            UserRepository userRepository
    ) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    // =================================================
    // 👷 WORKER → Apply for job
    // =================================================
    public String applyForJob(Long jobId, String workerEmail) {

        User worker = userRepository.findByEmail(workerEmail)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (applicationRepository.existsByWorkerAndJob(worker, job)) {
            throw new AlreadyAppliedException(
                    "You have already applied for this job"
            );
        }

        JobApplication application = JobApplication.builder()
                .worker(worker)
                .job(job)
                .build(); // status & appliedAt handled by @PrePersist

        applicationRepository.save(application);

        return "✅ Job applied successfully";
    }

    // =================================================
    // 👨‍💼 EMPLOYER → View applicants
    // =================================================
    public List<JobApplicantResponse> getApplicantsForJob(
            Long jobId,
            String employerEmail
    ) {

        Job job = jobRepository
                .findByIdAndEmployerEmail(jobId, employerEmail)
                .orElseThrow(() ->
                        new RuntimeException("Job not found or not owned by you")
                );

        return applicationRepository.findByJob(job)
                .stream()
                .map(app -> new JobApplicantResponse(
                        app.getId(),
                        app.getWorker().getId(),
                        app.getWorker().getName(),
                        app.getWorker().getEmail(),
                        app.getStatus(),
                        app.getAppliedAt()
                ))
                .toList();
    }

    // =================================================
    // 👷 WORKER → View My Applications
    // =================================================
    public List<JobApplicantResponse> getMyApplications(String workerEmail) {

        User worker = userRepository.findByEmail(workerEmail)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        return applicationRepository.findByWorkerEmail(workerEmail)
                .stream()
                .map(app -> new JobApplicantResponse(
                        app.getId(),
                        worker.getId(),
                        worker.getName(),
                        worker.getEmail(),
                        app.getStatus(),
                        app.getAppliedAt()
                ))
                .toList();
    }

    // =================================================
    // 👨‍💼 EMPLOYER → ACCEPT / REJECT
    // =================================================
    @Transactional
    public String updateApplicationStatus(
            Long jobId,
            Long applicationId,
            String action
    ) {

        String employerEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Job job = jobRepository
                .findByIdAndEmployerEmail(jobId, employerEmail)
                .orElseThrow(() ->
                        new RuntimeException("Job not found or not owned by you")
                );

        JobApplication selectedApplication = applicationRepository
                .findByIdAndJob(applicationId, job)
                .orElseThrow(() ->
                        new RuntimeException("Application not found")
                );

        if ("ACCEPT".equalsIgnoreCase(action)) {

            selectedApplication.setStatus(ApplicationStatus.ACCEPTED);

            List<JobApplication> allApplications =
                    applicationRepository.findByJob(job);

            for (JobApplication app : allApplications) {
                if (!app.getId().equals(applicationId)) {
                    app.setStatus(ApplicationStatus.REJECTED);
                }
            }

            job.setStatus(JobStatus.ASSIGNED);
            jobRepository.save(job);

        } else if ("REJECT".equalsIgnoreCase(action)) {

            selectedApplication.setStatus(ApplicationStatus.REJECTED);

        } else {
            throw new RuntimeException("Invalid action");
        }

        applicationRepository.save(selectedApplication);

        return "✅ Application " + action.toUpperCase() + "ED successfully";
    }
}
