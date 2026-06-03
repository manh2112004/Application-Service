package org.Application.command.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, String> {
    List<InterviewSchedule> findAllByApplicationIdIn(List<String> applicationIds);
}
