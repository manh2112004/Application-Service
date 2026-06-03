package org.Application.command.aggregate;

import org.Application.command.command.CreateApplicationCommand;
import org.Application.command.event.ApplicationCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class ApplicationAggregate {

    @AggregateIdentifier
    private String applicationId;

    public ApplicationAggregate() {
        // Required by Axon
    }

    @CommandHandler
    public ApplicationAggregate(CreateApplicationCommand command) {
        AggregateLifecycle.apply(ApplicationCreatedEvent.builder()
                .applicationId(command.getApplicationId())
                .candidateId(command.getCandidateId())
                .jobId(command.getJobId())
                .companyId(command.getCompanyId())
                .fullName(command.getFullName())
                .email(command.getEmail())
                .phoneNumber(command.getPhoneNumber())
                .currentJobTitle(command.getCurrentJobTitle())
                .coverLetter(command.getCoverLetter())
                .linkedinUrl(command.getLinkedinUrl())
                .portfolioUrl(command.getPortfolioUrl())
                .resumeFileUrl(command.getResumeFileUrl())
                .status(command.getStatus())
                .build());
    }

    @EventSourcingHandler
    public void on(ApplicationCreatedEvent event) {
        this.applicationId = event.getApplicationId();
    }
}
