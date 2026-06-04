package org.Application.command.event;

import org.Application.command.data.Application;
import org.Application.command.data.ApplicationRepository;
import org.Application.command.data.ApplicationStatusHistory;
import org.Application.command.data.ApplicationStatusHistoryRepository;
import org.Application.constant.ApplicationStatus;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ApplicationEventHandler {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationStatusHistoryRepository applicationStatusHistoryRepository;

    @EventHandler
    @Transactional
    public void on(ApplicationCreatedEvent event) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        Application application = Application.builder()
                .id(event.getApplicationId())
                .candidateId(event.getCandidateId())
                .fullName(event.getFullName())
                .email(event.getEmail())
                .phoneNumber(event.getPhoneNumber())
                .currentJobTitle(event.getCurrentJobTitle())
                .jobId(event.getJobId())
                .companyId(event.getCompanyId())
                .status(event.getStatus())
                .appliedDate(today)
                .coverLetter(event.getCoverLetter())
                .linkedinUrl(event.getLinkedinUrl())
                .portfolioUrl(event.getPortfolioUrl())
                .resumeFileUrl(event.getResumeFileUrl())
                .rating(0.0)
                .followUpRequested(false)
                .isDeleted(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        applicationRepository.save(application);
    }

    @EventHandler
    @Transactional
    public void on(ApplicationWithdrawnEvent event) {
        applicationRepository.findById(event.getApplicationId()).ifPresent(application -> {
            ApplicationStatus oldStatus = application.getStatus();
            application.setStatus(ApplicationStatus.WITHDRAWN);
            application.setUpdatedAt(LocalDateTime.now());
            applicationRepository.save(application);

            ApplicationStatusHistory history = ApplicationStatusHistory.builder()
                    .id(UUID.randomUUID().toString())
                    .applicationId(event.getApplicationId())
                    .oldStatus(oldStatus)
                    .newStatus(ApplicationStatus.WITHDRAWN)
                    .changedBy(event.getCandidateId())
                    .changedAt(LocalDateTime.now())
                    .build();
            applicationStatusHistoryRepository.save(history);
        });
    }
}
