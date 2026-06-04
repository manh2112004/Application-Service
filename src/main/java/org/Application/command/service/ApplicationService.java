package org.Application.command.service;

import org.Application.command.model.request.CreateApplicationRequest;
import java.util.concurrent.CompletableFuture;

public interface ApplicationService {
    CompletableFuture<String> createApplication(String candidateId, CreateApplicationRequest request);
    CompletableFuture<String> withdrawApplication(String candidateId, String applicationId);
}
