package org.Application.command.controller;

import jakarta.validation.Valid;
import org.Application.command.model.request.CreateApplicationRequest;
import org.Application.command.model.request.UpdateApplicationStatusRequest;
import org.Application.command.model.request.UpdateApplicationRatingRequest;
import org.Application.command.model.request.CreateApplicationNoteRequest;
import org.Application.command.model.request.ScheduleInterviewRequest;
import org.Application.command.service.ApplicationService;
import org.Application.constant.ApplicationStatus;
import org.Application.event.KafkaEvent;
import org.Application.event.KafkaEventProducer;
import org.Application.event.KafkaTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationCommandController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private KafkaEventProducer kafkaEventProducer;

    @PostMapping
    public CompletableFuture<String> createApplication(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateApplicationRequest request
    ) {
        return applicationService.createApplication(jwt.getSubject(), request).thenApply(applicationId -> {
            kafkaEventProducer.sendEvent(KafkaTopic.APPLICATION_EVENTS, KafkaEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType("ApplicationSubmittedEvent")
                    .userId(jwt.getSubject())
                    .referenceId(applicationId)
                    .referenceType("APPLICATION")
                    .title("Nộp hồ sơ thành công")
                    .message("Bạn đã nộp hồ sơ ứng tuyển thành công.")
                    .createdAt(LocalDateTime.now())
                    .build());
            return applicationId;
        });
    }

    @PutMapping("/{applicationId}/withdraw")
    public CompletableFuture<String> withdrawApplication(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String applicationId
    ) {
        return applicationService.withdrawApplication(jwt.getSubject(), applicationId).thenApply(result -> {
            kafkaEventProducer.sendEvent(KafkaTopic.APPLICATION_EVENTS, KafkaEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType("ApplicationWithdrawnEvent")
                    .userId(jwt.getSubject())
                    .referenceId(applicationId)
                    .referenceType("APPLICATION")
                    .title("Rút hồ sơ ứng tuyển")
                    .message("Bạn đã rút hồ sơ ứng tuyển thành công.")
                    .createdAt(LocalDateTime.now())
                    .build());
            return result;
        });
    }

    @PutMapping("/{applicationId}/review")
    public CompletableFuture<String> reviewApplication(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String applicationId
    ) {
        return applicationService.updateApplicationStatus(applicationId, jwt.getSubject(), ApplicationStatus.IN_REVIEW).thenApply(result -> {
            kafkaEventProducer.sendEvent(KafkaTopic.APPLICATION_EVENTS, KafkaEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType("ApplicationReviewedEvent")
                    .userId(jwt.getSubject())
                    .referenceId(applicationId)
                    .referenceType("APPLICATION")
                    .title("Hồ sơ ứng tuyển đang được xem xét")
                    .message("Hồ sơ ứng tuyển của bạn đang được xem xét.")
                    .createdAt(LocalDateTime.now())
                    .build());
            return result;
        });
    }

    @PutMapping("/{applicationId}/accept")
    public CompletableFuture<String> acceptApplication(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String applicationId
    ) {
        return applicationService.updateApplicationStatus(applicationId, jwt.getSubject(), ApplicationStatus.OFFERED).thenApply(result -> {
            kafkaEventProducer.sendEvent(KafkaTopic.APPLICATION_EVENTS, KafkaEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType("ApplicationAcceptedEvent")
                    .userId(jwt.getSubject())
                    .referenceId(applicationId)
                    .referenceType("APPLICATION")
                    .title("Hồ sơ ứng tuyển được chấp nhận")
                    .message("Chúc mừng! Hồ sơ ứng tuyển của bạn đã được chấp nhận.")
                    .createdAt(LocalDateTime.now())
                    .build());
            return result;
        });
    }

    @PutMapping("/{applicationId}/reject")
    public CompletableFuture<String> rejectApplication(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String applicationId
    ) {
        return applicationService.updateApplicationStatus(applicationId, jwt.getSubject(), ApplicationStatus.DECLINED).thenApply(result -> {
            kafkaEventProducer.sendEvent(KafkaTopic.APPLICATION_EVENTS, KafkaEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType("ApplicationRejectedEvent")
                    .userId(jwt.getSubject())
                    .referenceId(applicationId)
                    .referenceType("APPLICATION")
                    .title("Hồ sơ ứng tuyển bị từ chối")
                    .message("Rất tiếc, hồ sơ ứng tuyển của bạn đã bị từ chối.")
                    .createdAt(LocalDateTime.now())
                    .build());
            return result;
        });
    }

    @PutMapping("/{applicationId}/status")
    public CompletableFuture<String> updateApplicationStatus(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String applicationId,
            @Valid @RequestBody UpdateApplicationStatusRequest request
    ) {
        return applicationService.updateApplicationStatus(applicationId, jwt.getSubject(), request.getStatus()).thenApply(result -> {
            String eventType;
            String title;
            String message;
            if (request.getStatus() == ApplicationStatus.DECLINED || request.getStatus() == ApplicationStatus.UNSUITABLE) {
                eventType = "ApplicationRejectedEvent";
                title = "Hồ sơ ứng tuyển bị từ chối";
                message = "Rất tiếc, hồ sơ ứng tuyển của bạn đã bị từ chối.";
            } else if (request.getStatus() == ApplicationStatus.OFFERED || request.getStatus() == ApplicationStatus.HIRED) {
                eventType = "ApplicationAcceptedEvent";
                title = "Hồ sơ ứng tuyển được chấp nhận";
                message = "Chúc mừng! Hồ sơ ứng tuyển của bạn đã được chấp nhận.";
            } else {
                eventType = "ApplicationReviewedEvent";
                title = "Hồ sơ ứng tuyển đang được xem xét";
                message = "Hồ sơ ứng tuyển của bạn đang được xem xét.";
            }
            kafkaEventProducer.sendEvent(KafkaTopic.APPLICATION_EVENTS, KafkaEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType(eventType)
                    .userId(jwt.getSubject())
                    .referenceId(applicationId)
                    .referenceType("APPLICATION")
                    .title(title)
                    .message(message)
                    .createdAt(LocalDateTime.now())
                    .build());
            return result;
        });
    }

    @PutMapping("/{applicationId}/rating")
    public CompletableFuture<String> updateApplicationRating(
            @PathVariable String applicationId,
            @Valid @RequestBody UpdateApplicationRatingRequest request
    ) {
        return applicationService.updateApplicationRating(applicationId, request.getRating());
    }

    @PostMapping("/{applicationId}/notes")
    public CompletableFuture<String> addApplicationNote(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String applicationId,
            @Valid @RequestBody CreateApplicationNoteRequest request
    ) {
        return applicationService.addApplicationNote(applicationId, jwt.getSubject(), request);
    }

    @PostMapping("/{applicationId}/interviews")
    public CompletableFuture<String> scheduleInterview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String applicationId,
            @Valid @RequestBody ScheduleInterviewRequest request
    ) {
        return applicationService.scheduleInterview(applicationId, request);
    }
}
