package com.shkvlnc.bujo_app.service;

import com.shkvlnc.bujo_app.domain.Context;
import com.shkvlnc.bujo_app.domain.Project;
import com.shkvlnc.bujo_app.domain.Inbox;
import com.shkvlnc.bujo_app.domain.Tag;
import com.shkvlnc.bujo_app.dto.inbox.InboxCreateRequest;
import com.shkvlnc.bujo_app.dto.inbox.InboxResponse;
import com.shkvlnc.bujo_app.dto.inbox.InboxUpdateRequest;
import com.shkvlnc.bujo_app.repository.ContextRepository;
import com.shkvlnc.bujo_app.repository.ProjectRepository;
import com.shkvlnc.bujo_app.repository.InboxRepository;
import com.shkvlnc.bujo_app.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InboxService {
    private final InboxRepository inboxRepo;
    private final ProjectRepository projectRepo;
    private final ContextRepository contextRepo;
    private final TagRepository tagRepo;

    public List<InboxResponse> listAll() {
        return inboxRepo.findAll().stream()
                .map(InboxResponse::fromEntity) // ✅ consistent DTO mapping
                .toList();
    }

    public InboxResponse getById(Long id) {
        Inbox inbox = inboxRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inbox item not found"));
        return InboxResponse.fromEntity(inbox);
    }

    public InboxResponse create(InboxCreateRequest req) {
        Inbox inbox = new Inbox();
        applyCreateRequest(inbox, req);
        return InboxResponse.fromEntity(inboxRepo.save(inbox));
    }

    public InboxResponse update(Long id, InboxUpdateRequest req) {
        Inbox inbox = inboxRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inbox item not found"));
        applyUpdateRequest(inbox, req);
        return InboxResponse.fromEntity(inboxRepo.save(inbox));
    }

    public void delete(Long id) {
        if (!inboxRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inbox item not found");
        }
        inboxRepo.deleteById(id);
    }

    // ✅ Push search logic into repository for efficiency
    public List<InboxResponse> search(String keyword, String tag, Inbox.Status status) {
        List<Inbox> results = inboxRepo.findAll(); // could be optimized with custom queries
        return results.stream()
                .filter(inbox -> keyword == null || inbox.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .filter(inbox -> tag == null || inbox.getTags().stream()
                        .anyMatch(t -> t.getName().equalsIgnoreCase(tag)))
                .filter(inbox -> status == null || inbox.getStatus() == status)
                .map(InboxResponse::fromEntity)
                .toList();
    }

    // --- Helper methods ---

    private void applyCreateRequest(Inbox inbox, InboxCreateRequest req) {
        inbox.setTitle(req.getTitle());
        inbox.setDescription(req.getDescription());
        inbox.setDueDate(req.getDueDate());
        inbox.setPriority(req.getPriority());
        inbox.setStatus(req.getStatus() != null ? req.getStatus() : Inbox.Status.PENDING);
        inbox.setTags(resolveTags(req.getTags()));

        if (req.getProjectId() != null) {
            Project project = projectRepo.findById(req.getProjectId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Project not found with id: " + req.getProjectId()));
            inbox.setProject(project);
        }

        if (req.getContextId() != null) {
            Context context = contextRepo.findById(req.getContextId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Context not found with id: " + req.getContextId()));
            inbox.setContext(context);
        }
    }


    private void applyUpdateRequest(Inbox inbox, InboxUpdateRequest req) {
        inbox.setTitle(req.getTitle());
        inbox.setDescription(req.getDescription());
        inbox.setDueDate(req.getDueDate());
        inbox.setPriority(req.getPriority());
        inbox.setStatus(req.getStatus());
        inbox.setTags(resolveTags(req.getTags()));

        // ✅ Hybrid project resolution
        if (req.getProjectId() != null) {
            Project project = projectRepo.findById(req.getProjectId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Project not found with id: " + req.getProjectId()));
            inbox.setProject(project);
        } else if (req.getProjectName() != null) {
            Project project = projectRepo.findByNameIgnoreCase(req.getProjectName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Project not found with name: " + req.getProjectName()));
            inbox.setProject(project);
        } else {
            inbox.setProject(null);
        }

        // ✅ Hybrid context resolution
        if (req.getContextId() != null) {
            Context context = contextRepo.findById(req.getContextId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Context not found with id: " + req.getContextId()));
            inbox.setContext(context);
        } else if (req.getContextName() != null) {
            Context context = contextRepo.findByNameIgnoreCase(req.getContextName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Context not found with name: " + req.getContextName()));
            inbox.setContext(context);
        } else {
            inbox.setContext(null);
        }
    }

    private List<Tag> resolveTags(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) return List.of();
        return new ArrayList<>(
                tagNames.stream()
                        .map(name -> tagRepo.findByNameIgnoreCase(name)
                                .orElseGet(() -> tagRepo.save(new Tag(name))))
                        .toList()
        );
    }
}
