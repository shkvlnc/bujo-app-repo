package com.shkvlnc.bujo_app.dto.inbox;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import com.shkvlnc.bujo_app.domain.Inbox;
import com.shkvlnc.bujo_app.domain.Project;
import com.shkvlnc.bujo_app.domain.Tag;

import com.shkvlnc.bujo_app.repository.InboxRepository;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "status",
        "priority",
        "dueDate",
        "startDate",
        "completedDate",
        "contextId",
        "contextName",
        "projectId",
        "projectName",
        "tags"
})
@Getter
public class InboxResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final LocalDate dueDate;
    private final Inbox.Priority priority;
    private final Inbox.Status status;
    private final Long projectId;
    private final String projectName;
    private final Long contextId;
    private final String contextName;
    private final List<String> tags;
    private final LocalDate startDate;
    private final LocalDate completedDate;

    private InboxResponse(Long id, String title, String description, LocalDate dueDate,
                          Inbox.Priority priority, Inbox.Status status,
                          Long projectId, String projectName,
                          Long contextId, String contextName,
                          List<String> tags, LocalDate startDate, LocalDate completedDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.projectId = projectId;
        this.projectName = projectName;
        this.contextId = contextId;
        this.contextName = contextName;
        this.tags = tags;
        this.startDate = startDate;
        this.completedDate = completedDate;
    }

    public static InboxResponse fromEntity(Inbox inbox) {
        if (inbox == null) return null;

        return new InboxResponse(
                inbox.getId(),
                inbox.getTitle(),
                inbox.getDescription(),
                inbox.getDueDate(),
                inbox.getPriority(),
                inbox.getStatus(),
                inbox.getProject() != null ? inbox.getProject().getId() : null,
                inbox.getProject() != null ? inbox.getProject().getName() : null,
                inbox.getContext() != null ? inbox.getContext().getId() : null,
                inbox.getContext() != null ? inbox.getContext().getName() : null,
                inbox.getTags() != null ? inbox.getTags().stream().map(Tag::getName).toList() : List.of(),
                inbox.getStartDate(),
                inbox.getCompletedDate()
        );
    }
}
