package org.Application.command.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class UpdateApplicationRatingCommand {
    @TargetAggregateIdentifier
    private final String applicationId;
    private final Double rating;
}
