package org.Application.command.command;

import lombok.Builder;
import lombok.Data;
import org.Application.constant.ApplicationStatus;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class CreateApplicationCommand {
    @TargetAggregateIdentifier
    private final String applicationId;
    private final String candidateId;
    private final String jobId;
    private final String companyId;
    private final String fullName;
    private final String email;
    private final String phoneNumber;
    private final String currentJobTitle;
    private final String coverLetter;
    private final String linkedinUrl;
    private final String portfolioUrl;
    private final String resumeFileUrl;
    private final ApplicationStatus status;
}
