package com.sevasahayak.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🧑‍💼 Employer who posted job
    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private User employer;

    // 📝 Job details
    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    // 💰 Payment
    @Column(nullable = false)
    private Double salary;

    // 📍 Location
    private String location;

    // 📌 Job status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    // ⏰ Created time
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = JobStatus.OPEN;
    }
}
