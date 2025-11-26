package com.shkvlnc.bujo_app.dto;

import com.shkvlnc.bujo_app.domain.Project;
import lombok.Getter;

import java.util.List;

@Getter
public class ProjectResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final List<InboxResponse> inboxes;

    private ProjectResponse(Long id, String name, String description, List<InboxResponse> inboxes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.inboxes = inboxes;
    }

    public static ProjectResponse fromEntity(Project project) {
        if (project == null) {
            return null; // ✅ null-safe
        }
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getInboxes() != null
                        ? project.getInboxes().stream()
                        .map(InboxResponse::fromEntity)
                        .toList()
                        : List.of()
        );
    }
}
