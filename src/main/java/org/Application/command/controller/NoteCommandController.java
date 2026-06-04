package org.Application.command.controller;

import jakarta.validation.Valid;
import org.Application.command.model.request.UpdateApplicationNoteRequest;
import org.Application.command.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteCommandController {

    @Autowired
    private ApplicationService applicationService;

    @PutMapping("/{noteId}")
    public CompletableFuture<String> updateNote(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String noteId,
            @Valid @RequestBody UpdateApplicationNoteRequest request
    ) {
        return applicationService.updateApplicationNote(noteId, jwt.getSubject(), request);
    }
}
