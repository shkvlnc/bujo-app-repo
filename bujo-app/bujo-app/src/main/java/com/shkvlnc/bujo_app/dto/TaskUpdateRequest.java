package com.shkvlnc.bujo_app.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskUpdateRequest {
    @NotBlank
    private String title;
    @Size(max = 500)
    private String description;
    private LocalDate dueDate;
    @Min(1) @Max(5)
    private Integer priority;
    @Pattern(regexp = "PENDING|DONE")
    private String status;
    private Long projectId;
    private Long contextId;
    // getters/setters
}