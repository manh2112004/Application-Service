package org.Application.command.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Application.constant.ApplicationStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationCreatedEvent {
    private String applicationId;
    private String candidateId;
    private String jobId;
    private String companyId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String currentJobTitle;
    private String coverLetter;
    private String linkedinUrl;
    private String portfolioUrl;
    private String resumeFileUrl;
    private ApplicationStatus status;
}
