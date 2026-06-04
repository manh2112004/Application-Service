package org.Application.query.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Application.constant.ApplicationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationResponse {
    private String id;
    private String candidateId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String currentJobTitle;
    private String jobId;
    private String companyId;
    private ApplicationStatus status;
    private Double rating;
    private LocalDate appliedDate;
    private Boolean followUpRequested;
    private LocalDate followUpRequestedAt;
    private String coverLetter;
    private String linkedinUrl;
    private String portfolioUrl;
    private String resumeFileUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
