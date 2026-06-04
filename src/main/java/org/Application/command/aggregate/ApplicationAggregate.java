package org.Application.command.aggregate;

import org.Application.command.command.CreateApplicationCommand;
import org.Application.command.command.WithdrawApplicationCommand;
import org.Application.command.command.UpdateApplicationStatusCommand;
import org.Application.command.command.UpdateApplicationRatingCommand;
import org.Application.command.command.AddApplicationNoteCommand;
import org.Application.command.command.UpdateApplicationNoteCommand;
import org.Application.command.command.DeleteApplicationNoteCommand;
import org.Application.command.event.ApplicationCreatedEvent;
import org.Application.command.event.ApplicationWithdrawnEvent;
import org.Application.command.event.ApplicationStatusUpdatedEvent;
import org.Application.command.event.ApplicationRatingUpdatedEvent;
import org.Application.command.event.ApplicationNoteAddedEvent;
import org.Application.command.event.ApplicationNoteUpdatedEvent;
import org.Application.command.event.ApplicationNoteDeletedEvent;
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

    @CommandHandler
    public void handle(WithdrawApplicationCommand command) {
        AggregateLifecycle.apply(ApplicationWithdrawnEvent.builder()
                .applicationId(command.getApplicationId())
                .candidateId(command.getCandidateId())
                .build());
    }

    @CommandHandler
    public void handle(UpdateApplicationStatusCommand command) {
        AggregateLifecycle.apply(ApplicationStatusUpdatedEvent.builder()
                .applicationId(command.getApplicationId())
                .changedBy(command.getChangedBy())
                .status(command.getStatus())
                .build());
    }

    @CommandHandler
    public void handle(UpdateApplicationRatingCommand command) {
        AggregateLifecycle.apply(ApplicationRatingUpdatedEvent.builder()
                .applicationId(command.getApplicationId())
                .rating(command.getRating())
                .build());
    }

    @CommandHandler
    public void handle(AddApplicationNoteCommand command) {
        AggregateLifecycle.apply(ApplicationNoteAddedEvent.builder()
                .applicationId(command.getApplicationId())
                .noteId(command.getNoteId())
                .recruiterId(command.getRecruiterId())
                .recruiterName(command.getRecruiterName())
                .content(command.getContent())
                .build());
    }

    @CommandHandler
    public void handle(UpdateApplicationNoteCommand command) {
        AggregateLifecycle.apply(ApplicationNoteUpdatedEvent.builder()
                .applicationId(command.getApplicationId())
                .noteId(command.getNoteId())
                .content(command.getContent())
                .build());
    }

    @CommandHandler
    public void handle(DeleteApplicationNoteCommand command) {
        AggregateLifecycle.apply(ApplicationNoteDeletedEvent.builder()
                .applicationId(command.getApplicationId())
                .noteId(command.getNoteId())
                .build());
    }

    @EventSourcingHandler
    public void on(ApplicationCreatedEvent event) {
        this.applicationId = event.getApplicationId();
    }

    @EventSourcingHandler
    public void on(ApplicationWithdrawnEvent event) {
        this.applicationId = event.getApplicationId();
    }

    @EventSourcingHandler
    public void on(ApplicationStatusUpdatedEvent event) {
        this.applicationId = event.getApplicationId();
    }

    @EventSourcingHandler
    public void on(ApplicationRatingUpdatedEvent event) {
        this.applicationId = event.getApplicationId();
    }

    @EventSourcingHandler
    public void on(ApplicationNoteAddedEvent event) {
        this.applicationId = event.getApplicationId();
    }

    @EventSourcingHandler
    public void on(ApplicationNoteUpdatedEvent event) {
        this.applicationId = event.getApplicationId();
    }

    @EventSourcingHandler
    public void on(ApplicationNoteDeletedEvent event) {
        this.applicationId = event.getApplicationId();
    }
}
