package org.Application.query.controller;

import org.Application.constant.ApplicationStatus;
import org.Application.query.model.response.JobApplicationPageResponse;
import org.Application.query.model.response.JobPipelineResponse;
import org.Application.query.queries.GetJobApplicationsQuery;
import org.Application.query.queries.GetJobPipelineQuery;
import org.Application.query.queries.SearchJobApplicationsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobApplicationQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/{jobId}/applications")
    public CompletableFuture<JobApplicationPageResponse> getJobApplications(
            @PathVariable String jobId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ApplicationStatus status
    ) {
        return queryGateway.query(
                new GetJobApplicationsQuery(jobId, page, size, keyword, status),
                ResponseTypes.instanceOf(JobApplicationPageResponse.class)
        );
    }

    @GetMapping("/{jobId}/applications/search")
    public CompletableFuture<JobApplicationPageResponse> searchJobApplications(
            @PathVariable String jobId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return queryGateway.query(
                new SearchJobApplicationsQuery(jobId, keyword, page, size),
                ResponseTypes.instanceOf(JobApplicationPageResponse.class)
        );
    }

    @GetMapping("/{jobId}/pipeline")
    public CompletableFuture<JobPipelineResponse> getJobPipeline(@PathVariable String jobId) {
        return queryGateway.query(
                new GetJobPipelineQuery(jobId),
                ResponseTypes.instanceOf(JobPipelineResponse.class)
        );
    }
}
