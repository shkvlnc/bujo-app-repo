package com.shkvlnc.bujo_app.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import com.shkvlnc.bujo_app.domain.Inbox; // ✅ reuse enums from entity

@Getter @Setter
public class InboxUpdateRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private LocalDate dueDate; // ✅ optional for updates

    private Inbox.Priority priority; // ✅ enum instead of Integer

    private Inbox.Status status = Inbox.Status.PENDING; // ✅ reuse domain enum

    private Long projectId;
    private Long contextId;

    @Size(max = 10, message = "You can assign up to 10 tags")
    private List<String> tags;
}
