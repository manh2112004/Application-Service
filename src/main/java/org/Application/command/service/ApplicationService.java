package org.Application.command.service;

import org.Application.command.model.request.CreateApplicationRequest;
import org.Application.command.model.request.CreateApplicationNoteRequest;
import org.Application.command.model.request.UpdateApplicationNoteRequest;
import org.Application.command.model.request.ScheduleInterviewRequest;
import org.Application.command.model.request.UpdateInterviewRequest;
import org.Application.command.model.request.CreateInterviewFeedbackRequest;
import org.Application.constant.ApplicationStatus;
import java.util.concurrent.CompletableFuture;

public interface ApplicationService {
    CompletableFuture<String> createApplication(String candidateId, CreateApplicationRequest request);
    CompletableFuture<String> withdrawApplication(String candidateId, String applicationId);
    CompletableFuture<String> updateApplicationStatus(String applicationId, String changedBy, ApplicationStatus status);
    CompletableFuture<String> updateApplicationRating(String applicationId, Double rating);
    CompletableFuture<String> addApplicationNote(String applicationId, String recruiterId, CreateApplicationNoteRequest request);
    CompletableFuture<String> updateApplicationNote(String noteId, String recruiterId, UpdateApplicationNoteRequest request);
    CompletableFuture<String> deleteApplicationNote(String noteId, String recruiterId);
    CompletableFuture<String> scheduleInterview(String applicationId, ScheduleInterviewRequest request);
    CompletableFuture<String> updateInterview(String interviewId, UpdateInterviewRequest request);
    CompletableFuture<String> deleteInterview(String interviewId);
    CompletableFuture<String> addInterviewFeedback(String interviewId, String reviewerId, CreateInterviewFeedbackRequest request);
}
