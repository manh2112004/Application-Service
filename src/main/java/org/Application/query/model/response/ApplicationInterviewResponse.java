package org.Application.query.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Application.constant.InterviewStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationInterviewResponse {
    private String id;
    private String applicationId;
    private String interviewerId;
    private String interviewerName;
    private String title;
    private LocalDate interviewDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private InterviewStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
