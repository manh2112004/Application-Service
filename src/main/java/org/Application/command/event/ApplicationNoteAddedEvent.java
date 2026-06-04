package org.Application.command.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationNoteAddedEvent {
    private String applicationId;
    private String noteId;
    private String recruiterId;
    private String recruiterName;
    private String content;
}
