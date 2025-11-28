package com.shkvlnc.bujo_app.dto.inbox;

import com.shkvlnc.bujo_app.domain.Inbox;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class InboxUpdateRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 1000)
    private String description;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Priority is required")
    private Inbox.Priority priority;   // ✅ enum, consistent with domain

    private Inbox.Status status;       // ✅ drives lifecycle changes (startDate/completedDate auto-filled in service)

    private Long projectId;
    private String projectName;        // ✅ hybrid resolution: either ID or name

    private Long contextId;
    private String contextName;        // ✅ hybrid resolution: either ID or name

    private List<String> tags;
}
