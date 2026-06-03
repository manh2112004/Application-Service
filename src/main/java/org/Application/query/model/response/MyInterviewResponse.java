package org.Application.query.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Application.constant.InterviewStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyInterviewResponse {
    private String id;
    private String applicationId;
    private String jobTitle;
    private String companyName;
    private String companyLogoUrl;
    private String title;
    private LocalDate interviewDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private InterviewStatus status;
    private String interviewerName;
}
