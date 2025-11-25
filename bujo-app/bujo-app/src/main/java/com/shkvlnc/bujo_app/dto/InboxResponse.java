package com.shkvlnc.bujo_app.dto;

import com.shkvlnc.bujo_app.domain.Inbox;

import com.shkvlnc.bujo_app.domain.Tag;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class InboxResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Integer priority;
    private String status;
    private Long projectId;
    private String projectName;
    private Long contextId;
    private String contextName;
    private List<String> tags;
    // getters/setters

    public InboxResponse(Inbox inbox) {
        this.id = inbox.getId();
        this.title = inbox.getTitle();
        this.description = inbox.getDescription();
        this.dueDate = inbox.getDueDate();
        this.priority = inbox.getPriority();
        this.status = inbox.getStatus();

        if (inbox.getProject() != null) {
            this.projectId = inbox.getProject().getId();
            this.projectName = inbox.getProject().getName();
        }
        if (inbox.getContext() != null) {
            this.contextId = inbox.getContext().getId();
            this.contextName = inbox.getContext().getName();
        }
        this.tags = inbox.getTags().stream().map(Tag::getName).toList();
    }

}