package org.Application.command.data;

import jakarta.persistence.*;
import lombok.*;
import org.Application.constant.ApplicationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    @Id
    private String id;

    // lấy từ User/Profile Service
    @Column(nullable = false)
    private String candidateId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    private String currentJobTitle;

    // lấy từ Job Service
    @Column(nullable = false)
    private String jobId;

    // lấy từ Company Service
    @Column(nullable = false)
    private String companyId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    private Double rating;

    private LocalDate appliedDate;

    private Boolean followUpRequested;

    private LocalDate followUpRequestedAt;

    private String coverLetter;

    private String linkedinUrl;

    private String portfolioUrl;

    private String resumeFileUrl;

    private Boolean isDeleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}