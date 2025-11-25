package com.shkvlnc.bujo_app.service;


import com.shkvlnc.bujo_app.domain.Context;
import com.shkvlnc.bujo_app.domain.Project;
import com.shkvlnc.bujo_app.domain.Task;
import com.shkvlnc.bujo_app.dto.TaskCreateRequest;
import com.shkvlnc.bujo_app.dto.TaskResponse;
import com.shkvlnc.bujo_app.dto.TaskUpdateRequest;
import com.shkvlnc.bujo_app.repository.ContextRepository;
import com.shkvlnc.bujo_app.repository.ProjectRepository;
import com.shkvlnc.bujo_app.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepo;
    private final ProjectRepository projectRepo;
    private final ContextRepository contextRepo;

    public TaskService(TaskRepository taskRepo, ProjectRepository projectRepo, ContextRepository contextRepo) {
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
        this.contextRepo = contextRepo;
    }

    public List<TaskResponse> listAll() {
        return taskRepo.findAll().stream().map(this::toResponse).toList();
    }

    public List<TaskResponse> listByStatus(String status) {
        return taskRepo.findByStatus(status).stream().map(this::toResponse).toList();
    }

    public TaskResponse create(TaskCreateRequest req) {
        Task t = new Task();
        t.setTitle(req.getTitle());
        t.setDescription(req.getDescription());
        t.setDueDate(req.getDueDate());
        t.setPriority(req.getPriority());
        t.setStatus(Optional.ofNullable(req.getStatus()).orElse("PENDING"));

        if (req.getProjectId() != null) {
            Project p = projectRepo.findById(req.getProjectId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
            t.setProject(p);
        }
        if (req.getContextId() != null) {
            Context c = contextRepo.findById(req.getContextId())
                    .orElseThrow(() -> new IllegalArgumentException("Context not found"));
            t.setContext(c);
        }

        Task saved = taskRepo.save(t);
        return toResponse(saved);
    }

    public TaskResponse update(Long id, TaskUpdateRequest req) {
        Task t = taskRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        t.setTitle(req.getTitle());
        t.setDescription(req.getDescription());
        t.setDueDate(req.getDueDate());
        t.setPriority(req.getPriority());
        t.setStatus(req.getStatus());

        if (req.getProjectId() != null) {
            Project p = projectRepo.findById(req.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("Project not found"));
            t.setProject(p);
        } else {
            t.setProject(null);
        }
        if (req.getContextId() != null) {
            Context c = contextRepo.findById(req.getContextId())
                    .orElseThrow(() -> new IllegalArgumentException("Context not found"));
            t.setContext(c);
        } else {
            t.setContext(null);
        }

        return toResponse(t);
    }

    public void delete(Long id) {
        taskRepo.deleteById(id);
    }

    private TaskResponse toResponse(Task t) {
        TaskResponse resp = new TaskResponse();
        resp.setId(t.getId());
        resp.setTitle(t.getTitle());
        resp.setDescription(t.getDescription());
        resp.setDueDate(t.getDueDate());
        resp.setPriority(t.getPriority());
        resp.setStatus(t.getStatus());
        if (t.getProject() != null) {
            resp.setProjectId(t.getProject().getId());
            resp.setProjectName(t.getProject().getName());
        }
        if (t.getContext() != null) {
            resp.setContextId(t.getContext().getId());
            resp.setContextName(t.getContext().getName());
        }
        return resp;
    }
}