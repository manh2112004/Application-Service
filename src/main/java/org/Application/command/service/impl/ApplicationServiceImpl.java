package org.Application.command.service.impl;

import org.Application.command.command.CreateApplicationCommand;
import org.Application.command.data.ApplicationRepository;
import org.Application.command.model.request.CreateApplicationRequest;
import org.Application.command.service.ApplicationService;
import org.Application.constant.ApplicationStatus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private org.Application.client.JobClient jobClient;

    @Autowired
    private org.Application.client.CompanyClient companyClient;

    @Override
    public CompletableFuture<String> createApplication(String candidateId, CreateApplicationRequest request) {
        if (candidateId == null || candidateId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được người dùng từ token");
        }

        // 1. Validate if the company exists
        org.Application.client.dto.CompanyResponse company = companyClient.getCompany(request.getCompanyId().trim());
        if (company == null || company.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại");
        }

        // 2. Validate if the job exists and matches the companyId
        org.Application.client.dto.JobResponse job = jobClient.getJob(request.getJobId().trim());
        if (job == null || job.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Công việc không tồn tại");
        }
        if (!job.getCompanyId().equals(request.getCompanyId().trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Công việc này không thuộc về công ty được cung cấp");
        }

        // 2. Validate if application already exists for this job and candidate
        boolean alreadyApplied = applicationRepository.existsByCandidateIdAndJobId(
                candidateId, request.getJobId().trim());
        if (alreadyApplied) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bạn đã ứng tuyển vào công việc này trước đó");
        }

        CreateApplicationCommand command = CreateApplicationCommand.builder()
                .applicationId(UUID.randomUUID().toString())
                .candidateId(candidateId)
                .jobId(request.getJobId().trim())
                .companyId(request.getCompanyId().trim())
                .fullName(request.getFullName().trim())
                .email(request.getEmail().trim())
                .phoneNumber(request.getPhoneNumber().trim())
                .currentJobTitle(request.getCurrentJobTitle() != null ? request.getCurrentJobTitle().trim() : null)
                .coverLetter(request.getCoverLetter() != null ? request.getCoverLetter().trim() : null)
                .linkedinUrl(request.getLinkedinUrl() != null ? request.getLinkedinUrl().trim() : null)
                .portfolioUrl(request.getPortfolioUrl() != null ? request.getPortfolioUrl().trim() : null)
                .resumeFileUrl(request.getResumeFileUrl() != null ? request.getResumeFileUrl().trim() : null)
                .status(ApplicationStatus.IN_REVIEW) // Initial status when applying
                .build();

        return commandGateway.send(command);
    }
}
