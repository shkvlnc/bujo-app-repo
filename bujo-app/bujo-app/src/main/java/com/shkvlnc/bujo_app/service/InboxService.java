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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.shkvlnc.bujo_app.domain.Inbox.Priority.*;

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

        // Basic fields
        inbox.setTitle(req.getTitle());
        inbox.setDescription(req.getDescription());
        inbox.setDueDate(req.getDueDate());
        inbox.setPriority(req.getPriority());

        // ✅ Lifecycle handling for status
        if (req.getStatus() != null) {
            if (req.getStatus() == Inbox.Status.IN_PROGRESS && inbox.getStartDate() == null) {
                inbox.setStartDate(LocalDate.now()); // auto-fill startDate
            }
            if (req.getStatus() == Inbox.Status.DONE && inbox.getCompletedDate() == null) {
                inbox.setCompletedDate(LocalDate.now()); // auto-fill completedDate
            }
            inbox.setStatus(req.getStatus());
        }

        // Tags
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

        return InboxResponse.fromEntity(inboxRepo.save(inbox));
    }

    public void delete(Long id) {
        if (!inboxRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inbox item not found");
        }
        inboxRepo.deleteById(id);
    }

    public List<InboxResponse> search(String keyword, String tag, Inbox.Status status,
                                      Long contextId, Long projectId) {
        return inboxRepo.search(keyword, tag, status, contextId, projectId)
                .stream()
                .map(InboxResponse::fromEntity)
                .toList();
    }

    private void applyCreateRequest(Inbox inbox, InboxCreateRequest req) {
        inbox.setTitle(req.getTitle());
        inbox.setDescription(req.getDescription());
        inbox.setDueDate(req.getDueDate());
        inbox.setPriority(req.getPriority());
        inbox.setStatus(req.getStatus() != null ? req.getStatus() : Inbox.Status.PENDING);
        inbox.setTags(resolveTags(req.getTags()));

        if (req.getProjectId() != null) {
            Project project = projectRepo.findById(req.getProjectId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Project not found. Please create the project first."));
            inbox.setProject(project);
        }

        if (req.getContextId() != null) {
            Context context = contextRepo.findById(req.getContextId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Context not found. Please create the context first."));
            inbox.setContext(context);
        }

        if (req.getStatus() == Inbox.Status.IN_PROGRESS) {
            inbox.setStartDate(LocalDate.now());
        }
        if (req.getStatus() == Inbox.Status.DONE) {
            inbox.setCompletedDate(LocalDate.now());
        }
    }


    private void applyUpdateRequest(Inbox inbox, InboxUpdateRequest req) {
        inbox.setTitle(req.getTitle());
        inbox.setDescription(req.getDescription());
        inbox.setDueDate(req.getDueDate());
        inbox.setPriority(req.getPriority());
        inbox.setStatus(req.getStatus());
        inbox.setTags(resolveTags(req.getTags()));

        if (req.getProjectId() != null) {
            Project project = projectRepo.findById(req.getProjectId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Project not found. Please create the project first."));
            inbox.setProject(project);
        } else {
            inbox.setProject(null);
        }

        if (req.getContextId() != null) {
            Context context = contextRepo.findById(req.getContextId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Context not found. Please create the context first."));
            inbox.setContext(context);
        } else {
            inbox.setContext(null);
        }

        if (req.getStatus() == Inbox.Status.IN_PROGRESS && inbox.getStartDate() == null) {
            inbox.setStartDate(LocalDate.now());
        }
        if (req.getStatus() == Inbox.Status.DONE && inbox.getCompletedDate() == null) {
            inbox.setCompletedDate(LocalDate.now());
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

    public List<InboxResponse> listAllOrdered() {
        return inboxRepo.findAll().stream()
                .map(InboxResponse::fromEntity)
                .sorted(taskOrder())   // ✅ apply comparator
                .toList();
    }

    private Project resolveProject(Long projectId, String projectName) {
        if (projectId != null) {
            return projectRepo.findById(projectId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Project not found. Please create the project first."));
        } else if (projectName != null) {
            return projectRepo.findByNameIgnoreCase(projectName)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Project not found. Please create the project first."));
        }
        return null;
    }

    private Context resolveContext(Long contextId, String contextName) {
        if (contextId != null) {
            return contextRepo.findById(contextId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Context not found. Please create the context first."));
        } else if (contextName != null) {
            return contextRepo.findByNameIgnoreCase(contextName)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Context not found. Please create the context first."));
        }
        return null;
    }

    private Comparator<InboxResponse> taskOrder() {
        return Comparator
                .comparing(InboxResponse::getStatus, Comparator.comparing(
                        status -> status == Inbox.Status.PENDING ? 0 : 1))
                .thenComparing(InboxResponse::getPriority, Comparator.comparingInt(priority -> {
                    return switch (priority) {
                        case CRITICAL -> 5;
                        case URGENT   -> 4;
                        case HIGH     -> 3;
                        case MEDIUM   -> 2;
                        case LOW      -> 1;
                        default       -> 0;
                    };
                }).reversed())
                .thenComparing(InboxResponse::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(InboxResponse::getContextName, Comparator.nullsLast(String::compareToIgnoreCase))
                .thenComparing(InboxResponse::getProjectName, Comparator.nullsLast(String::compareToIgnoreCase))
                .thenComparing(InboxResponse::getTitle, String::compareToIgnoreCase);
    }
}
