package org.Application.command.controller;

import jakarta.validation.Valid;
import org.Application.command.model.request.CreateApplicationRequest;
import org.Application.command.model.request.UpdateApplicationStatusRequest;
import org.Application.command.model.request.UpdateApplicationRatingRequest;
import org.Application.command.model.request.CreateApplicationNoteRequest;
import org.Application.command.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationCommandController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public CompletableFuture<String> createApplication(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateApplicationRequest request
    ) {
        return applicationService.createApplication(jwt.getSubject(), request);
    }

    @PutMapping("/{applicationId}/withdraw")
    public CompletableFuture<String> withdrawApplication(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String applicationId
    ) {
        return applicationService.withdrawApplication(jwt.getSubject(), applicationId);
    }

    @PutMapping("/{applicationId}/status")
    public CompletableFuture<String> updateApplicationStatus(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String applicationId,
            @Valid @RequestBody UpdateApplicationStatusRequest request
    ) {
        return applicationService.updateApplicationStatus(applicationId, jwt.getSubject(), request.getStatus());
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
}
