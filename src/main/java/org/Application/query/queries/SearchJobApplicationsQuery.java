package org.Application.query.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchJobApplicationsQuery {
    private String jobId;
    private String keyword;
    private int page;
    private int size;
}
