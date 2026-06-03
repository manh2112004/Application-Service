package org.Application.command.data;
import jakarta.persistence.*;
import lombok.*;
import org.Application.constant.ApplicationStatus;

import java.time.LocalDateTime;
@Entity
@Table(name = "application_status_histories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationStatusHistory {

    @Id
    private String id;

    private String applicationId;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus oldStatus;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus newStatus;

    private String changedBy;

    private LocalDateTime changedAt;
}
