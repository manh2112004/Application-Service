package org.Application.query.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationNoteResponse {
    private String id;
    private String applicationId;
    private String recruiterId;
    private String recruiterName;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
