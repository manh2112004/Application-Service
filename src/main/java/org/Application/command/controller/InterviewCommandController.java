package org.Application.command.controller;

import jakarta.validation.Valid;
import org.Application.command.model.request.UpdateInterviewRequest;
import org.Application.command.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
