package org.Application.query.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Application.constant.ApplicationStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyApplicationHistoryResponse {
    private String id;
    private String applicationId;
    private ApplicationStatus oldStatus;
    private ApplicationStatus newStatus;
    private String changedBy;
    private LocalDateTime changedAt;
}
