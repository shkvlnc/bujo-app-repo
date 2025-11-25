package com.shkvlnc.bujo_app.dto;

import com.shkvlnc.bujo_app.domain.Project;
import lombok.Data;

import java.util.List;

@Data
public class ProjectResponse {
    private Long id;
    private String name;
    private List<InboxResponse> tasks;

    public ProjectResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.tasks = project.getInboxes().stream()
                .map(InboxResponse::new)
                .toList();
    }

    // getters...
}