package org.Application.command.service.impl;

import org.Application.command.command.CreateApplicationCommand;
import org.Application.command.command.WithdrawApplicationCommand;
import org.Application.command.command.UpdateApplicationStatusCommand;
import org.Application.command.command.UpdateApplicationRatingCommand;
import org.Application.command.command.AddApplicationNoteCommand;
import org.Application.command.command.UpdateApplicationNoteCommand;
import org.Application.command.command.DeleteApplicationNoteCommand;
import org.Application.command.command.ScheduleInterviewCommand;
import org.Application.command.data.Application;
import org.Application.command.data.ApplicationRepository;
import org.Application.command.data.ApplicationNote;
import org.Application.command.data.ApplicationNoteRepository;
import org.Application.command.model.request.CreateApplicationRequest;
import org.Application.command.model.request.CreateApplicationNoteRequest;
import org.Application.command.model.request.UpdateApplicationNoteRequest;
import org.Application.command.model.request.ScheduleInterviewRequest;
import org.Application.command.service.ApplicationService;
import org.Application.constant.ApplicationStatus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationNoteRepository applicationNoteRepository;

    @Autowired
    private org.Application.client.JobClient jobClient;

    @Autowired
    private org.Application.client.CompanyClient companyClient;

    @Autowired
    private org.Application.client.ProfileClient profileClient;

    @Override
    public CompletableFuture<String> createApplication(String candidateId, CreateApplicationRequest request) {
        if (candidateId == null || candidateId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được người dùng từ token");
        }

        // 1. Validate if the company exists
        org.Application.client.dto.CompanyResponse company = companyClient.getCompany(request.getCompanyId().trim());
        if (company == null || company.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại");
        }

        // 2. Validate if the job exists and matches the companyId
        org.Application.client.dto.JobResponse job = jobClient.getJob(request.getJobId().trim());
        if (job == null || job.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Công việc không tồn tại");
        }
        if (!job.getCompanyId().equals(request.getCompanyId().trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Công việc này không thuộc về công ty được cung cấp");
        }

        // 2. Validate if application already exists for this job and candidate
        boolean alreadyApplied = applicationRepository.existsByCandidateIdAndJobId(
                candidateId, request.getJobId().trim());
        if (alreadyApplied) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bạn đã ứng tuyển vào công việc này trước đó");
        }

        CreateApplicationCommand command = CreateApplicationCommand.builder()
                .applicationId(UUID.randomUUID().toString())
                .candidateId(candidateId)
                .jobId(request.getJobId().trim())
                .companyId(request.getCompanyId().trim())
                .fullName(request.getFullName().trim())
                .email(request.getEmail().trim())
                .phoneNumber(request.getPhoneNumber().trim())
                .currentJobTitle(request.getCurrentJobTitle() != null ? request.getCurrentJobTitle().trim() : null)
                .coverLetter(request.getCoverLetter() != null ? request.getCoverLetter().trim() : null)
                .linkedinUrl(request.getLinkedinUrl() != null ? request.getLinkedinUrl().trim() : null)
                .portfolioUrl(request.getPortfolioUrl() != null ? request.getPortfolioUrl().trim() : null)
                .resumeFileUrl(request.getResumeFileUrl() != null ? request.getResumeFileUrl().trim() : null)
                .status(ApplicationStatus.IN_REVIEW) // Initial status when applying
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> withdrawApplication(String candidateId, String applicationId) {
        if (candidateId == null || candidateId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được người dùng từ token");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển"));

        if (Boolean.TRUE.equals(application.getIsDeleted())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển");
        }

        if (!application.getCandidateId().equals(candidateId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền rút hồ sơ này");
        }

        if (application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hồ sơ ứng tuyển đã được rút trước đó");
        }

        WithdrawApplicationCommand command = WithdrawApplicationCommand.builder()
                .applicationId(applicationId)
                .candidateId(candidateId)
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> updateApplicationStatus(String applicationId, String changedBy, ApplicationStatus status) {
        if (changedBy == null || changedBy.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được người dùng từ token");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển"));

        if (Boolean.TRUE.equals(application.getIsDeleted())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển");
        }

        UpdateApplicationStatusCommand command = UpdateApplicationStatusCommand.builder()
                .applicationId(applicationId)
                .changedBy(changedBy)
                .status(status)
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> updateApplicationRating(String applicationId, Double rating) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển"));

        if (Boolean.TRUE.equals(application.getIsDeleted())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển");
        }

        UpdateApplicationRatingCommand command = UpdateApplicationRatingCommand.builder()
                .applicationId(applicationId)
                .rating(rating)
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> addApplicationNote(String applicationId, String recruiterId, CreateApplicationNoteRequest request) {
        if (recruiterId == null || recruiterId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được người dùng từ token");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển"));

        if (Boolean.TRUE.equals(application.getIsDeleted())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển");
        }

        String name = request.getRecruiterName() != null && !request.getRecruiterName().isBlank() 
                ? request.getRecruiterName().trim() 
                : "Nhà tuyển dụng";

        AddApplicationNoteCommand command = AddApplicationNoteCommand.builder()
                .applicationId(applicationId)
                .noteId(UUID.randomUUID().toString())
                .recruiterId(recruiterId)
                .recruiterName(name)
                .content(request.getContent().trim())
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> updateApplicationNote(String noteId, String recruiterId, UpdateApplicationNoteRequest request) {
        if (recruiterId == null || recruiterId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được người dùng từ token");
        }

        ApplicationNote note = applicationNoteRepository.findById(noteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ghi chú"));

        if (!note.getRecruiterId().equals(recruiterId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền chỉnh sửa ghi chú này");
        }

        // Validate that the application exists and is not deleted
        Application application = applicationRepository.findById(note.getApplicationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển liên quan"));

        if (Boolean.TRUE.equals(application.getIsDeleted())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển liên quan");
        }

        UpdateApplicationNoteCommand command = UpdateApplicationNoteCommand.builder()
                .applicationId(note.getApplicationId())
                .noteId(noteId)
                .content(request.getContent().trim())
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> deleteApplicationNote(String noteId, String recruiterId) {
        if (recruiterId == null || recruiterId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không xác định được người dùng từ token");
        }

        ApplicationNote note = applicationNoteRepository.findById(noteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ghi chú"));

        if (!note.getRecruiterId().equals(recruiterId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa ghi chú này");
        }

        // Validate that the application exists and is not deleted
        Application application = applicationRepository.findById(note.getApplicationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển liên quan"));

        if (Boolean.TRUE.equals(application.getIsDeleted())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển liên quan");
        }

        DeleteApplicationNoteCommand command = DeleteApplicationNoteCommand.builder()
                .applicationId(note.getApplicationId())
                .noteId(noteId)
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<String> scheduleInterview(String applicationId, ScheduleInterviewRequest request) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển"));

        if (Boolean.TRUE.equals(application.getIsDeleted())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển");
        }

        // Validate if interviewer exists in Profile-Service
        org.Application.client.dto.ProfileResponse profile = profileClient.getProfileByUserId(request.getInterviewerId().trim());
        if (profile == null || profile.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người phỏng vấn không tồn tại");
        }

        String interviewerName = profile.getFullName() != null && !profile.getFullName().isBlank()
                ? profile.getFullName().trim()
                : "Người phỏng vấn";

        ScheduleInterviewCommand command = ScheduleInterviewCommand.builder()
                .applicationId(applicationId)
                .interviewId(UUID.randomUUID().toString())
                .interviewerId(request.getInterviewerId().trim())
                .interviewerName(interviewerName)
                .title(request.getTitle().trim())
                .interviewDate(request.getInterviewDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .location(request.getLocation().trim())
                .build();

        return commandGateway.send(command);
    }
}
