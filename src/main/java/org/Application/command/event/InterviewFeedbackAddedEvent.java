package org.Application.command.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewFeedbackAddedEvent {
    private String applicationId;
    private String feedbackId;
    private String interviewScheduleId;
    private String reviewerId;
    private String reviewerName;
    private Double score;
    private String comment;
}
