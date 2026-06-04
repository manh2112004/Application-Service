package org.Application.command.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.Application.constant.ApplicationStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateApplicationStatusRequest {
    @NotNull(message = "Trạng thái không được để trống")
    private ApplicationStatus status;
}
