package org.Application.command.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Application.constant.ApplicationStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusUpdatedEvent {
    private String applicationId;
    private String changedBy;
    private ApplicationStatus status;
}
