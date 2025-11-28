package com.shkvlnc.bujo_app.dto.project;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.shkvlnc.bujo_app.domain.Project;
import com.shkvlnc.bujo_app.dto.inbox.InboxResponse;
import lombok.Getter;

import java.util.List;

@JsonPropertyOrder({ "name", "id", "description", "inboxes" })
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
            return null;
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
