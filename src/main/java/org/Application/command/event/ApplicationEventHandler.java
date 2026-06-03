package org.Application.command.event;

import org.Application.command.data.Application;
import org.Application.command.data.ApplicationRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class ApplicationEventHandler {

    @Autowired
    private ApplicationRepository applicationRepository;

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
}
