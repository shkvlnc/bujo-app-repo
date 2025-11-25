package com.shkvlnc.bujo_app.service;


import com.shkvlnc.bujo_app.domain.Context;
import com.shkvlnc.bujo_app.domain.Project;
import com.shkvlnc.bujo_app.domain.Inbox;
import com.shkvlnc.bujo_app.dto.InboxCreateRequest;
import com.shkvlnc.bujo_app.dto.InboxResponse;
import com.shkvlnc.bujo_app.dto.InboxUpdateRequest;
import com.shkvlnc.bujo_app.repository.ContextRepository;
import com.shkvlnc.bujo_app.repository.ProjectRepository;
import com.shkvlnc.bujo_app.repository.InboxRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InboxService {
    private final InboxRepository inboxRepo;
    private final ProjectRepository projectRepo;
    private final ContextRepository contextRepo;

    public InboxService(InboxRepository inboxRepo, ProjectRepository projectRepo, ContextRepository contextRepo) {
        this.inboxRepo = inboxRepo;
        this.projectRepo = projectRepo;
        this.contextRepo = contextRepo;
    }

    public List<InboxResponse> listAll() {
        return inboxRepo.findAll().stream().map(this::toResponse).toList();
    }

    public List<InboxResponse> listByStatus(String status) {
        return inboxRepo.findByStatus(status).stream().map(this::toResponse).toList();
    }

    public InboxResponse getById(Long id) {
        Inbox inbox = inboxRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inbox item not found"));
        return new InboxResponse(inbox);
    }

    public InboxResponse create(InboxCreateRequest req) {
        Inbox t = new Inbox();
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

        Inbox saved = inboxRepo.save(t);
        return toResponse(saved);
    }

    public InboxResponse update(Long id, InboxUpdateRequest req) {
        Inbox t = inboxRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
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
        inboxRepo.deleteById(id);
    }

    private InboxResponse toResponse(Inbox t) {
        return new InboxResponse(t);
//        InboxResponse resp = new InboxResponse(t);
//        resp.setId(t.getId());
//        resp.setTitle(t.getTitle());
//        resp.setDescription(t.getDescription());
//        resp.setDueDate(t.getDueDate());
//        resp.setPriority(t.getPriority());
//        resp.setStatus(t.getStatus());
//        if (t.getProject() != null) {
//            resp.setProjectId(t.getProject().getId());
//            resp.setProjectName(t.getProject().getName());
//        }
//        if (t.getContext() != null) {
//            resp.setContextId(t.getContext().getId());
//            resp.setContextName(t.getContext().getName());
//        }
//        return resp;
    }
}