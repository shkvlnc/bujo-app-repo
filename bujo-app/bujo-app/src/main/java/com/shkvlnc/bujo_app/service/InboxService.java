package com.shkvlnc.bujo_app.service;


import com.shkvlnc.bujo_app.domain.Context;
import com.shkvlnc.bujo_app.domain.Project;
import com.shkvlnc.bujo_app.domain.Inbox;
import com.shkvlnc.bujo_app.domain.Tag;
import com.shkvlnc.bujo_app.dto.InboxCreateRequest;
import com.shkvlnc.bujo_app.dto.InboxResponse;
import com.shkvlnc.bujo_app.dto.InboxUpdateRequest;
import com.shkvlnc.bujo_app.repository.ContextRepository;
import com.shkvlnc.bujo_app.repository.ProjectRepository;
import com.shkvlnc.bujo_app.repository.InboxRepository;
import com.shkvlnc.bujo_app.repository.TagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InboxService {
    private final InboxRepository inboxRepo;
    private final ProjectRepository projectRepo;
    private final ContextRepository contextRepo;
    private final TagRepository tagRepo;

    public InboxService(InboxRepository inboxRepo,
                        ProjectRepository projectRepo,
                        ContextRepository contextRepo,
                        TagRepository tagRepo) {
        this.inboxRepo = inboxRepo;
        this.projectRepo = projectRepo;
        this.contextRepo = contextRepo;
        this.tagRepo = tagRepo;
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
        t.setTags(resolveTags(req.getTags()));

        if (req.getTags() != null && !req.getTags().isEmpty()) {
            t.setTags(resolveTags(req.getTags()));
        }

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
        Inbox t = inboxRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Task not found"));
        t.setTitle(req.getTitle());
        t.setDescription(req.getDescription());
        t.setDueDate(req.getDueDate());
        t.setPriority(req.getPriority());
        t.setStatus(req.getStatus());
        t.setTags(resolveTags(req.getTags()));

        if (req.getTags() != null && !req.getTags().isEmpty()) {
            t.setTags(resolveTags(req.getTags()));
        }

        if (req.getProjectId() != null) {
            Project p = projectRepo.findById(req.getProjectId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
            t.setProject(p);
        } else {
            t.setProject(null);
        }
        if (req.getContextId() != null) {
            Context c = contextRepo.findById(req.getContextId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Context not found"));
            t.setContext(c);
        } else {
            t.setContext(null);
        }

        Inbox saved = inboxRepo.save(t);
        return toResponse(saved);
    }

    public void delete(Long id) {
        inboxRepo.deleteById(id);
    }

    private InboxResponse toResponse(Inbox t) {
        return new InboxResponse(t);
    }

    private List<Tag> resolveTags(List<String> tagNames) {
        if (tagNames == null) return List.of();
        return new ArrayList<>(
                tagNames.stream()
                        .map(name -> tagRepo.findByNameIgnoreCase(name)
                                .orElseGet(() -> tagRepo.save(new Tag(name)))
                        )
                        .toList()
        );
    }
}