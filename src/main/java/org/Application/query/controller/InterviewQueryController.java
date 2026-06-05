package org.Application.query.controller;

import org.Application.query.model.response.ApplicationInterviewResponse;
import org.Application.query.model.response.InterviewFeedbackListResponse;
import org.Application.query.queries.GetInterviewDetailQuery;
import org.Application.query.queries.GetInterviewFeedbacksQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/interviews")
public class InterviewQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/{interviewId}")
    public CompletableFuture<ApplicationInterviewResponse> getInterviewDetail(@PathVariable String interviewId) {
        return queryGateway.query(
                new GetInterviewDetailQuery(interviewId),
                ResponseTypes.instanceOf(ApplicationInterviewResponse.class)
        );
    }

    @GetMapping("/{interviewId}/feedbacks")
    public CompletableFuture<InterviewFeedbackListResponse> getInterviewFeedbacks(@PathVariable String interviewId) {
        return queryGateway.query(
                new GetInterviewFeedbacksQuery(interviewId),
                ResponseTypes.instanceOf(InterviewFeedbackListResponse.class)
        );
    }
}
