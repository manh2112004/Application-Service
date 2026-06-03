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
public class MyApplicationResponse {
    private String id;
    private String jobId;
    private String companyId;
    private String jobTitle;
    private String companyName;
    private String companyLogoUrl;
    private LocalDate appliedDate;
    private ApplicationStatus status;
    private Double rating;
    private String resumeFileUrl;
    private String coverLetter;
    private LocalDateTime createdAt;
}
