package org.Application.query.controller;

import org.Application.query.model.response.RecruiterDashboardResponse;
import org.Application.query.queries.GetRecruiterDashboardQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/recruiter")
public class RecruiterApplicationQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/dashboard")
    public CompletableFuture<RecruiterDashboardResponse> getRecruiterDashboard(@RequestParam String companyId) {
        return queryGateway.query(
                new GetRecruiterDashboardQuery(companyId),
                ResponseTypes.instanceOf(RecruiterDashboardResponse.class)
        );
    }
}
