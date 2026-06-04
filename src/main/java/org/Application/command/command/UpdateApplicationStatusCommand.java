package org.Application.command.command;

import lombok.Builder;
import lombok.Data;
import org.Application.constant.ApplicationStatus;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class UpdateApplicationStatusCommand {
    @TargetAggregateIdentifier
    private final String applicationId;
    private final String changedBy;
    private final ApplicationStatus status;
}
