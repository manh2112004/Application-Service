package org.Application.query.queries;

import lombok.extern.slf4j.Slf4j;
import org.Application.client.CompanyClient;
import org.Application.client.JobClient;
import org.Application.client.dto.CompanyResponse;
import org.Application.client.dto.JobResponse;
import org.Application.command.data.Application;
import org.Application.command.data.ApplicationRepository;
import org.Application.command.data.InterviewSchedule;
import org.Application.command.data.InterviewScheduleRepository;
import org.Application.command.data.ApplicationStatusHistory;
import org.Application.command.data.ApplicationStatusHistoryRepository;
import org.Application.query.model.response.MyApplicationResponse;
import org.Application.query.model.response.MyApplicationHistoryResponse;
import org.Application.query.model.response.MyApplicationHistoryListResponse;
import org.Application.query.model.response.MyApplicationsListResponse;
import org.Application.query.model.response.MyDashboardResponse;
import org.Application.query.model.response.MyInterviewResponse;
import org.Application.query.model.response.MyInterviewsListResponse;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.Application.query.model.response.JobApplicationResponse;
import org.Application.query.model.response.JobApplicationPageResponse;
import org.Application.query.queries.GetJobApplicationsQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ApplicationQueryHandler {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private InterviewScheduleRepository interviewScheduleRepository;

    @Autowired
    private ApplicationStatusHistoryRepository applicationStatusHistoryRepository;

    @Autowired
    private JobClient jobClient;

    @Autowired
    private CompanyClient companyClient;

    @QueryHandler
    @Transactional(readOnly = true)
    public JobApplicationPageResponse handle(GetJobApplicationsQuery query) {
        if (query.getPage() < 1) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "page phải >= 1");
        }
        if (query.getSize() <= 0 || query.getSize() > 100) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "size phải trong khoảng 1..100");
        }

        Pageable pageable = PageRequest.of(
                query.getPage() - 1, // converting 1-indexed to 0-indexed
                query.getSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Specification<Application> spec = Specification.where((root, cq, cb) ->
                cb.and(
                        cb.equal(root.get("jobId"), query.getJobId()),
                        cb.equal(root.get("isDeleted"), false)
                )
        );

        if (query.getKeyword() != null && !query.getKeyword().isBlank()) {
            String keyword = "%" + query.getKeyword().toLowerCase().trim() + "%";
            spec = spec.and((root, cq, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("fullName")), keyword),
                            cb.like(cb.lower(root.get("email")), keyword)
                    )
            );
        }

        if (query.getStatus() != null) {
            spec = spec.and((root, cq, cb) ->
                    cb.equal(root.get("status"), query.getStatus())
            );
        }

        Page<Application> appPage = applicationRepository.findAll(spec, pageable);

        List<JobApplicationResponse> list = appPage.getContent().stream()
                .map(app -> JobApplicationResponse.builder()
                        .id(app.getId())
                        .candidateId(app.getCandidateId())
                        .fullName(app.getFullName())
                        .email(app.getEmail())
                        .phoneNumber(app.getPhoneNumber())
                        .currentJobTitle(app.getCurrentJobTitle())
                        .jobId(app.getJobId())
                        .companyId(app.getCompanyId())
                        .status(app.getStatus())
                        .rating(app.getRating())
                        .appliedDate(app.getAppliedDate())
                        .followUpRequested(app.getFollowUpRequested())
                        .followUpRequestedAt(app.getFollowUpRequestedAt())
                        .coverLetter(app.getCoverLetter())
                        .linkedinUrl(app.getLinkedinUrl())
                        .portfolioUrl(app.getPortfolioUrl())
                        .resumeFileUrl(app.getResumeFileUrl())
                        .createdAt(app.getCreatedAt())
                        .updatedAt(app.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        return new JobApplicationPageResponse(
                list,
                appPage.getNumber() + 1,
                appPage.getSize(),
                appPage.getTotalElements(),
                appPage.getTotalPages()
        );
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public MyApplicationsListResponse handle(GetMyApplicationsQuery query) {
        List<Application> applications = applicationRepository.findAllByCandidateIdAndIsDeletedFalse(query.getCandidateId());

        Map<String, JobResponse> jobCache = new HashMap<>();
        Map<String, CompanyResponse> companyCache = new HashMap<>();

        List<MyApplicationResponse> list = applications.stream()
                .sorted((a, b) -> {
                    if (a.getCreatedAt() == null || b.getCreatedAt() == null) return 0;
                    return b.getCreatedAt().compareTo(a.getCreatedAt());
                })
                .map(app -> {
                    // Enrich Job details
                    JobResponse job = null;
                    try {
                        job = jobCache.computeIfAbsent(app.getJobId(), id -> jobClient.getJob(id));
                        log.info("Fetched job details for jobId={}: {}", app.getJobId(), job);
                    } catch (Exception e) {
                        log.warn("Failed to fetch job details for jobId={} in query: {}", app.getJobId(), e.getMessage());
                    }

                    // Enrich Company details
                    CompanyResponse company = null;
                    try {
                        company = companyCache.computeIfAbsent(app.getCompanyId(), id -> companyClient.getCompany(id));
                    } catch (Exception e) {
                        log.warn("Failed to fetch company details for companyId={} in query: {}", app.getCompanyId(), e.getMessage());
                    }

                    return MyApplicationResponse.builder()
                            .id(app.getId())
                            .jobId(app.getJobId())
                            .companyId(app.getCompanyId())
                            .jobTitle(job != null ? job.getTitle() : "Công việc không khả dụng")
                            .companyName(company != null ? company.getCompanyName() : "Công ty không khả dụng")
                            .companyLogoUrl(company != null ? company.getLogoUrl() : null)
                            .appliedDate(app.getAppliedDate())
                            .status(app.getStatus())
                            .rating(app.getRating())
                            .resumeFileUrl(app.getResumeFileUrl())
                            .coverLetter(app.getCoverLetter())
                            .createdAt(app.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return new MyApplicationsListResponse(list);
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public MyApplicationResponse handle(GetMyApplicationDetailQuery query) {
        Application app = applicationRepository.findById(query.getApplicationId())
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển"));

        if (Boolean.TRUE.equals(app.getIsDeleted())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển");
        }

        if (!app.getCandidateId().equals(query.getCandidateId())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập hồ sơ này");
        }

        // Enrich Job details
        JobResponse job = null;
        try {
            job = jobClient.getJob(app.getJobId());
        } catch (Exception e) {
            log.warn("Failed to fetch job details for jobId={} in query: {}", app.getJobId(), e.getMessage());
        }

        // Enrich Company details
        CompanyResponse company = null;
        try {
            company = companyClient.getCompany(app.getCompanyId());
        } catch (Exception e) {
            log.warn("Failed to fetch company details for companyId={} in query: {}", app.getCompanyId(), e.getMessage());
        }

        return MyApplicationResponse.builder()
                .id(app.getId())
                .jobId(app.getJobId())
                .companyId(app.getCompanyId())
                .jobTitle(job != null ? job.getTitle() : "Công việc không khả dụng")
                .companyName(company != null ? company.getCompanyName() : "Công ty không khả dụng")
                .companyLogoUrl(company != null ? company.getLogoUrl() : null)
                .appliedDate(app.getAppliedDate())
                .status(app.getStatus())
                .rating(app.getRating())
                .resumeFileUrl(app.getResumeFileUrl())
                .coverLetter(app.getCoverLetter())
                .createdAt(app.getCreatedAt())
                .build();
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public MyDashboardResponse handle(GetMyDashboardQuery query) {
        List<Application> applications = applicationRepository.findAllByCandidateIdAndIsDeletedFalse(query.getCandidateId());

        long total = applications.size();
        long inReview = 0;
        long shortlisted = 0;
        long interview = 0;
        long assessment = 0;
        long offered = 0;
        long hired = 0;
        long declined = 0;
        long unsuitable = 0;

        for (Application app : applications) {
            if (app.getStatus() == null) continue;
            switch (app.getStatus()) {
                case IN_REVIEW -> inReview++;
                case SHORTLISTED -> shortlisted++;
                case INTERVIEW -> interview++;
                case ASSESSMENT -> assessment++;
                case OFFERED -> offered++;
                case HIRED -> hired++;
                case DECLINED -> declined++;
                case UNSUITABLE -> unsuitable++;
                case WITHDRAWN -> {}
            }
        }

        return MyDashboardResponse.builder()
                .totalApplications(total)
                .inReviewCount(inReview)
                .shortlistedCount(shortlisted)
                .interviewCount(interview)
                .assessmentCount(assessment)
                .offeredCount(offered)
                .hiredCount(hired)
                .declinedCount(declined)
                .unsuitableCount(unsuitable)
                .build();
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public MyInterviewsListResponse handle(GetMyInterviewsQuery query) {
        List<Application> applications = applicationRepository.findAllByCandidateIdAndIsDeletedFalse(query.getCandidateId());
        if (applications.isEmpty()) {
            return new MyInterviewsListResponse(java.util.Collections.emptyList());
        }

        List<String> applicationIds = applications.stream()
                .map(Application::getId)
                .collect(Collectors.toList());

        List<InterviewSchedule> schedules = interviewScheduleRepository.findAllByApplicationIdIn(applicationIds);

        Map<String, Application> appMap = applications.stream()
                .collect(Collectors.toMap(Application::getId, app -> app));

        Map<String, JobResponse> jobCache = new HashMap<>();
        Map<String, CompanyResponse> companyCache = new HashMap<>();

        List<MyInterviewResponse> list = schedules.stream()
                .sorted((a, b) -> {
                    if (a.getInterviewDate() == null || b.getInterviewDate() == null) return 0;
                    int dateCompare = b.getInterviewDate().compareTo(a.getInterviewDate());
                    if (dateCompare != 0) return dateCompare;
                    if (a.getStartTime() == null || b.getStartTime() == null) return 0;
                    return b.getStartTime().compareTo(a.getStartTime());
                })
                .map(sch -> {
                    Application app = appMap.get(sch.getApplicationId());
                    String jobTitle = "Công việc không khả dụng";
                    String companyName = "Công ty không khả dụng";
                    String companyLogoUrl = null;

                    if (app != null) {
                        // Enrich Job details
                        try {
                            JobResponse job = jobCache.computeIfAbsent(app.getJobId(), id -> jobClient.getJob(id));
                            if (job != null) {
                                jobTitle = job.getTitle();
                            }
                        } catch (Exception e) {
                            log.warn("Failed to fetch job details for jobId={} in interviews query: {}", app.getJobId(), e.getMessage());
                        }

                        // Enrich Company details
                        try {
                            CompanyResponse company = companyCache.computeIfAbsent(app.getCompanyId(), id -> companyClient.getCompany(id));
                            if (company != null) {
                                companyName = company.getCompanyName();
                                companyLogoUrl = company.getLogoUrl();
                            }
                        } catch (Exception e) {
                            log.warn("Failed to fetch company details for companyId={} in interviews query: {}", app.getCompanyId(), e.getMessage());
                        }
                    }

                    return MyInterviewResponse.builder()
                            .id(sch.getId())
                            .applicationId(sch.getApplicationId())
                            .jobTitle(jobTitle)
                            .companyName(companyName)
                            .companyLogoUrl(companyLogoUrl)
                            .title(sch.getTitle())
                            .interviewDate(sch.getInterviewDate())
                            .startTime(sch.getStartTime())
                            .endTime(sch.getEndTime())
                            .location(sch.getLocation())
                            .status(sch.getStatus())
                            .interviewerName(sch.getInterviewerName())
                            .build();
                })
                .collect(Collectors.toList());

        return new MyInterviewsListResponse(list);
    }

    @QueryHandler
    @Transactional(readOnly = true)
    public MyApplicationHistoryListResponse handle(GetMyApplicationHistoryQuery query) {
        Application app = applicationRepository.findById(query.getApplicationId())
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển"));

        if (Boolean.TRUE.equals(app.getIsDeleted())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển");
        }

        if (!app.getCandidateId().equals(query.getCandidateId())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập hồ sơ này");
        }

        List<ApplicationStatusHistory> histories = applicationStatusHistoryRepository
                .findAllByApplicationIdOrderByChangedAtDesc(query.getApplicationId());

        List<MyApplicationHistoryResponse> list = histories.stream()
                .map(history -> MyApplicationHistoryResponse.builder()
                        .id(history.getId())
                        .applicationId(history.getApplicationId())
                        .oldStatus(history.getOldStatus())
                        .newStatus(history.getNewStatus())
                        .changedBy(history.getChangedBy())
                        .changedAt(history.getChangedAt())
                        .build())
                .collect(Collectors.toList());

        return new MyApplicationHistoryListResponse(list);
    }
}
