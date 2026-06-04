package org.Application.command.controller;

import jakarta.validation.Valid;
import org.Application.command.model.request.CreateApplicationRequest;
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
}
