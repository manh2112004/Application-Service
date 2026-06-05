package org.Application.command.controller;

import jakarta.validation.Valid;
import org.Application.command.model.request.UpdateInterviewRequest;
import org.Application.command.model.request.CreateInterviewFeedbackRequest;
import org.Application.command.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/interviews")
public class InterviewCommandController {

    @Autowired
    private ApplicationService applicationService;

    @PutMapping("/{interviewId}")
    public CompletableFuture<String> updateInterview(
            @PathVariable String interviewId,
            @Valid @RequestBody UpdateInterviewRequest request
    ) {
        return applicationService.updateInterview(interviewId, request);
    }

    @DeleteMapping("/{interviewId}")
    public CompletableFuture<String> deleteInterview(@PathVariable String interviewId) {
        return applicationService.deleteInterview(interviewId);
    }

    @PostMapping("/{interviewId}/feedbacks")
    public CompletableFuture<String> addInterviewFeedback(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String interviewId,
            @Valid @RequestBody CreateInterviewFeedbackRequest request
    ) {
        return applicationService.addInterviewFeedback(interviewId, jwt.getSubject(), request);
    }
}
