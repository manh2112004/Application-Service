package org.Application.command.data;
import jakarta.persistence.*;
import lombok.*;
import org.Application.constant.InterviewStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "interview_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewSchedule {

    @Id
    private String id;

    @Column(nullable = false)
    private String applicationId;

    private String interviewerId;

    private String interviewerName;

    private String title;
    // Written Test, Skill Test, Final Test

    private LocalDate interviewDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String location;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}