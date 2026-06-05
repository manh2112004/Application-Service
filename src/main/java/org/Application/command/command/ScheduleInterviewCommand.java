package org.Application.command.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ScheduleInterviewCommand {
    @TargetAggregateIdentifier
    private final String applicationId;
    private final String interviewId;
    private final String interviewerId;
    private final String interviewerName;
    private final String title;
    private final String location;
    private final LocalDate interviewDate;
    private final LocalTime startTime;
    private final LocalTime endTime;
}
