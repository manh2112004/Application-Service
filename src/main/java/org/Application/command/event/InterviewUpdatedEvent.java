package org.Application.command.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Application.constant.InterviewStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewUpdatedEvent {
    private String applicationId;
    private String interviewId;
    private String interviewerId;
    private String interviewerName;
    private String title;
    private String location;
    private LocalDate interviewDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private InterviewStatus status;
}
