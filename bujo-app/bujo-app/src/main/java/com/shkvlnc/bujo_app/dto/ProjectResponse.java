package com.shkvlnc.bujo_app.dto;

import com.shkvlnc.bujo_app.domain.Project;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private List<InboxResponse> inboxes;

    public ProjectResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.inboxes = project.getInboxes().stream()
                .map(InboxResponse::new)
                .collect(Collectors.toList());
    }

    // getters...
}