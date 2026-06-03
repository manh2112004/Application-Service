package org.Application.query.controller;

import org.Application.query.model.response.MyApplicationsListResponse;
import org.Application.query.model.response.MyApplicationResponse;
import org.Application.query.model.response.MyDashboardResponse;
import org.Application.query.model.response.MyInterviewsListResponse;
import org.Application.query.queries.GetMyApplicationDetailQuery;
import org.Application.query.queries.GetMyApplicationsQuery;
import org.Application.query.queries.GetMyDashboardQuery;
import org.Application.query.queries.GetMyInterviewsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/my")
    public CompletableFuture<MyApplicationsListResponse> getMyApplications(@AuthenticationPrincipal Jwt jwt) {
        return queryGateway.query(
                new GetMyApplicationsQuery(jwt.getSubject()),
                ResponseTypes.instanceOf(MyApplicationsListResponse.class)
        );
    }

    @GetMapping("/my/dashboard")
    public CompletableFuture<MyDashboardResponse> getMyDashboard(@AuthenticationPrincipal Jwt jwt) {
        return queryGateway.query(
                new GetMyDashboardQuery(jwt.getSubject()),
                ResponseTypes.instanceOf(MyDashboardResponse.class)
        );
    }

    @GetMapping("/my/interviews")
    public CompletableFuture<MyInterviewsListResponse> getMyInterviews(@AuthenticationPrincipal Jwt jwt) {
        return queryGateway.query(
                new GetMyInterviewsQuery(jwt.getSubject()),
                ResponseTypes.instanceOf(MyInterviewsListResponse.class)
        );
    }

    @GetMapping("/my/{applicationId}")
    public CompletableFuture<MyApplicationResponse> getMyApplication(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String applicationId
    ) {
        return queryGateway.query(
                new GetMyApplicationDetailQuery(applicationId, jwt.getSubject()),
                ResponseTypes.instanceOf(MyApplicationResponse.class)
        );
    }
}
