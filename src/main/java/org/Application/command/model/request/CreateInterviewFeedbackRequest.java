package org.Application.command.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInterviewFeedbackRequest {
    @NotNull(message = "Điểm đánh giá không được để trống")
    private Double score;

    @NotBlank(message = "Nhận xét không được để trống")
    private String comment;
}
