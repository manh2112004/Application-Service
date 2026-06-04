package org.Application.query.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationNoteListResponse {
    private List<ApplicationNoteResponse> notes;
}
