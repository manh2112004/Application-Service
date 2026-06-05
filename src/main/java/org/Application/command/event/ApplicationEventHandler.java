package org.Application.command.event;

import org.Application.command.data.Application;
import org.Application.command.data.ApplicationRepository;
import org.Application.command.data.ApplicationStatusHistory;
import org.Application.command.data.ApplicationStatusHistoryRepository;
import org.Application.command.data.ApplicationNote;
import org.Application.command.data.ApplicationNoteRepository;
import org.Application.command.data.InterviewSchedule;
import org.Application.command.data.InterviewScheduleRepository;
import org.Application.constant.ApplicationStatus;
import org.Application.constant.InterviewStatus;
import org.Application.command.event.InterviewScheduledEvent;
import org.Application.command.event.InterviewUpdatedEvent;
import org.Application.command.event.InterviewDeletedEvent;
import org.Application.command.data.InterviewFeedback;
import org.Application.command.data.InterviewFeedbackRepository;
import org.Application.command.event.InterviewFeedbackAddedEvent;
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

    @Autowired
    private ApplicationNoteRepository applicationNoteRepository;

    @Autowired
    private InterviewScheduleRepository interviewScheduleRepository;

    @Autowired
    private InterviewFeedbackRepository interviewFeedbackRepository;

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

    @EventHandler
    @Transactional
    public void on(ApplicationStatusUpdatedEvent event) {
        applicationRepository.findById(event.getApplicationId()).ifPresent(application -> {
            ApplicationStatus oldStatus = application.getStatus();
            application.setStatus(event.getStatus());
            application.setUpdatedAt(LocalDateTime.now());
            applicationRepository.save(application);

            ApplicationStatusHistory history = ApplicationStatusHistory.builder()
                    .id(UUID.randomUUID().toString())
                    .applicationId(event.getApplicationId())
                    .oldStatus(oldStatus)
                    .newStatus(event.getStatus())
                    .changedBy(event.getChangedBy())
                    .changedAt(LocalDateTime.now())
                    .build();
            applicationStatusHistoryRepository.save(history);
        });
    }

    @EventHandler
    @Transactional
    public void on(ApplicationRatingUpdatedEvent event) {
        applicationRepository.findById(event.getApplicationId()).ifPresent(application -> {
            application.setRating(event.getRating());
            application.setUpdatedAt(LocalDateTime.now());
            applicationRepository.save(application);
        });
    }

    @EventHandler
    @Transactional
    public void on(ApplicationNoteAddedEvent event) {
        LocalDateTime now = LocalDateTime.now();
        ApplicationNote note = ApplicationNote.builder()
                .id(event.getNoteId())
                .applicationId(event.getApplicationId())
                .recruiterId(event.getRecruiterId())
                .recruiterName(event.getRecruiterName())
                .content(event.getContent())
                .createdAt(now)
                .updatedAt(now)
                .build();
        applicationNoteRepository.save(note);
    }

    @EventHandler
    @Transactional
    public void on(ApplicationNoteUpdatedEvent event) {
        applicationNoteRepository.findById(event.getNoteId()).ifPresent(note -> {
            note.setContent(event.getContent());
            note.setUpdatedAt(LocalDateTime.now());
            applicationNoteRepository.save(note);
        });
    }

    @EventHandler
    @Transactional
    public void on(ApplicationNoteDeletedEvent event) {
        applicationNoteRepository.deleteById(event.getNoteId());
    }

    @EventHandler
    @Transactional
    public void on(InterviewScheduledEvent event) {
        LocalDateTime now = LocalDateTime.now();
        InterviewSchedule schedule = InterviewSchedule.builder()
                .id(event.getInterviewId())
                .applicationId(event.getApplicationId())
                .interviewerId(event.getInterviewerId())
                .interviewerName(event.getInterviewerName())
                .title(event.getTitle())
                .location(event.getLocation())
                .interviewDate(event.getInterviewDate())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .status(InterviewStatus.SCHEDULED)
                .createdAt(now)
                .updatedAt(now)
                .build();
        interviewScheduleRepository.save(schedule);
    }

    @EventHandler
    @Transactional
    public void on(InterviewUpdatedEvent event) {
        interviewScheduleRepository.findById(event.getInterviewId()).ifPresent(schedule -> {
            schedule.setInterviewerId(event.getInterviewerId());
            schedule.setInterviewerName(event.getInterviewerName());
            schedule.setTitle(event.getTitle());
            schedule.setLocation(event.getLocation());
            schedule.setInterviewDate(event.getInterviewDate());
            schedule.setStartTime(event.getStartTime());
            schedule.setEndTime(event.getEndTime());
            schedule.setStatus(event.getStatus());
            schedule.setUpdatedAt(LocalDateTime.now());
            interviewScheduleRepository.save(schedule);
        });
    }

    @EventHandler
    @Transactional
    public void on(InterviewDeletedEvent event) {
        interviewScheduleRepository.deleteById(event.getInterviewId());
    }

    @EventHandler
    @Transactional
    public void on(InterviewFeedbackAddedEvent event) {
        LocalDateTime now = LocalDateTime.now();
        InterviewFeedback feedback = InterviewFeedback.builder()
                .id(event.getFeedbackId())
                .interviewScheduleId(event.getInterviewScheduleId())
                .applicationId(event.getApplicationId())
                .reviewerId(event.getReviewerId())
                .reviewerName(event.getReviewerName())
                .score(event.getScore())
                .comment(event.getComment())
                .createdAt(now)
                .updatedAt(now)
                .build();
        interviewFeedbackRepository.save(feedback);
    }
}
