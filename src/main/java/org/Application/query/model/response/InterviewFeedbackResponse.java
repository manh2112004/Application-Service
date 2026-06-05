package org.Application.query.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewFeedbackResponse {
    private String id;
    private String interviewScheduleId;
    private String applicationId;
    private String reviewerId;
    private String reviewerName;
    private Double score;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
