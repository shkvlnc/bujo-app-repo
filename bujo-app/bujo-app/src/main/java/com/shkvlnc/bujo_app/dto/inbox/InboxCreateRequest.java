package com.shkvlnc.bujo_app.dto.inbox;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import com.shkvlnc.bujo_app.domain.Inbox;

@Getter @Setter
public class InboxCreateRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    private Inbox.Priority priority;

    private Inbox.Status status = Inbox.Status.PENDING;

    @Schema(description = "Optional. If you don’t want tags, just omit this field or send an empty array []")
    @Size(max = 10, message = "You can assign up to 10 tags")
    private List<String> tags;

    private Long projectId;
    private Long contextId;

}
