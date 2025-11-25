package com.shkvlnc.bujo_app.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskResponse {
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
    // getters/setters
}