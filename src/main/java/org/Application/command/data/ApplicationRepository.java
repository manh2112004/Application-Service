package org.Application.command.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String>, JpaSpecificationExecutor<Application> {
    boolean existsByCandidateIdAndJobId(String candidateId, String jobId);
    java.util.List<Application> findAllByCandidateIdAndIsDeletedFalse(String candidateId);
}
