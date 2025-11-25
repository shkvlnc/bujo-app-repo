package com.shkvlnc.bujo_app.web;


import com.shkvlnc.bujo_app.dto.InboxCreateRequest;
import com.shkvlnc.bujo_app.dto.InboxResponse;
import com.shkvlnc.bujo_app.dto.InboxUpdateRequest;
import com.shkvlnc.bujo_app.repository.InboxRepository;
import com.shkvlnc.bujo_app.service.InboxService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inbox")
public class InboxController {
    private final InboxService inboxService;

    private final InboxRepository inboxRepo;

//    public InboxController(InboxService inboxService, InboxRepository inboxRepo) {
//        this.inboxService = inboxService;
//        this.inboxRepo = inboxRepo;
//    }

    @GetMapping
    public List<InboxResponse> list() {
        return inboxService.listAll();
    }

    @GetMapping("/status/{status}")
    public List<InboxResponse> listByStatus(@PathVariable String status) {
        return inboxService.listByStatus(status);
    }

    @GetMapping("/{id}")
    public InboxResponse getById(@PathVariable Long id) {
        return inboxService.getById(id);
    }

    @PostMapping
    public ResponseEntity<InboxResponse> create(@Valid @RequestBody InboxCreateRequest req) {
        return ResponseEntity.ok(inboxService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InboxResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody InboxUpdateRequest req) {
        return ResponseEntity.ok(inboxService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inboxService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<InboxResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tag) {

        if (keyword != null) {
            return inboxRepo.findByTitleContainingIgnoreCase(keyword)
                    .stream().map(InboxResponse::new).toList();
        }
        if (tag != null) {
            return inboxRepo.findByTags_NameIgnoreCase(tag)
                    .stream().map(InboxResponse::new).toList();
        }
        return List.of();
    }
}