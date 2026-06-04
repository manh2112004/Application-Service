package org.Application.query.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Application.constant.ApplicationStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetJobApplicationsQuery {
    private String jobId;
    private int page;
    private int size;
    private String keyword;
    private ApplicationStatus status;
}
