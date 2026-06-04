package org.Application.query.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationPageResponse {
    private List<JobApplicationResponse> applications;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
