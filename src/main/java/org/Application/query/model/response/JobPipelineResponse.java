package org.Application.query.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPipelineResponse {
    private String jobId;
    private long totalCount;
    private long inReviewCount;
    private long shortlistedCount;
    private long interviewCount;
    private long assessmentCount;
    private long offeredCount;
    private long hiredCount;
    private long declinedCount;
    private long unsuitableCount;
    private long withdrawnCount;
}
