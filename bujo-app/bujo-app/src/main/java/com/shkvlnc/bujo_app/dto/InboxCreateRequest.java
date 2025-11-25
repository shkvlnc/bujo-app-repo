package com.shkvlnc.bujo_app.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class InboxCreateRequest {
    @NotBlank
    private String title;

    @Size(max = 500)
    private String description;

    private LocalDate dueDate;

    @Min(1) @Max(5)
    private Integer priority;

    @Pattern(regexp = "PENDING|DONE")
    private String status = "PENDING";

//    private Long projectId;
    private String projectName;

    private Long contextId;

    private List<String> tags;

    // getters/setters
}