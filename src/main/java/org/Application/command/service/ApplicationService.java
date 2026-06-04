package org.Application.command.service;

import org.Application.command.model.request.CreateApplicationRequest;
import org.Application.constant.ApplicationStatus;
import java.util.concurrent.CompletableFuture;

public interface ApplicationService {
    CompletableFuture<String> createApplication(String candidateId, CreateApplicationRequest request);
    CompletableFuture<String> withdrawApplication(String candidateId, String applicationId);
    CompletableFuture<String> updateApplicationStatus(String applicationId, String changedBy, ApplicationStatus status);
}
