package org.Application.command.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateApplicationNoteRequest {
    @NotBlank(message = "Nội dung ghi chú không được để trống")
    private String content;
}
