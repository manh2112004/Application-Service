package org.Application.query.queries;

import lombok.extern.slf4j.Slf4j;
import org.Application.client.CompanyClient;
import org.Application.client.JobClient;
import org.Application.client.dto.CompanyResponse;
import org.Application.client.dto.JobResponse;
import org.Application.command.data.Application;
import org.Application.command.data.ApplicationRepository;
import org.Application.query.model.response.MyApplicationResponse;
import org.Application.query.model.response.MyApplicationsListResponse;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private JobClient jobClient;

    @Autowired
    private CompanyClient companyClient;

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
}
