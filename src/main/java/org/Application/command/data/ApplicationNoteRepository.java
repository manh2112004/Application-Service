package org.Application.command.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationNoteRepository extends JpaRepository<ApplicationNote, String> {
    List<ApplicationNote> findAllByApplicationIdOrderByCreatedAtDesc(String applicationId);
}
