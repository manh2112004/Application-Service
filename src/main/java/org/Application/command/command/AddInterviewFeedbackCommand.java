package org.Application.command.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class AddInterviewFeedbackCommand {
    @TargetAggregateIdentifier
    private final String applicationId;
    private final String feedbackId;
    private final String interviewScheduleId;
    private final String reviewerId;
    private final String reviewerName;
    private final Double score;
    private final String comment;
}
