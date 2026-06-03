package org.Application.query.controller;

import org.Application.query.model.response.MyApplicationsListResponse;
import org.Application.query.queries.GetMyApplicationsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
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
}
