package org.Application.command.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewFeedbackRepository extends JpaRepository<InterviewFeedback, String> {
    List<InterviewFeedback> findAllByInterviewScheduleId(String interviewScheduleId);
}
