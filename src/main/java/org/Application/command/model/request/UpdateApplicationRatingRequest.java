package org.Application.command.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateApplicationRatingRequest {
    @NotNull(message = "Điểm đánh giá không được để trống")
    @Min(value = 0, message = "Điểm đánh giá thấp nhất là 0")
    @Max(value = 5, message = "Điểm đánh giá cao nhất là 5")
    private Double rating;
}
