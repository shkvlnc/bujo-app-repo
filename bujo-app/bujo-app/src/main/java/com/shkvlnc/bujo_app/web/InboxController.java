package com.shkvlnc.bujo_app.web;

import com.shkvlnc.bujo_app.domain.Inbox;
import com.shkvlnc.bujo_app.dto.inbox.InboxCreateRequest;
import com.shkvlnc.bujo_app.dto.inbox.InboxResponse;
import com.shkvlnc.bujo_app.dto.inbox.InboxUpdateRequest;
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

@GetMapping
public List<InboxResponse> list() {
    return inboxService.listAllOrdered();
}


    @GetMapping("/{id}")
    public InboxResponse getById(@PathVariable Long id) {
        return inboxService.getById(id);
    }

    @PostMapping
    public ResponseEntity<InboxResponse> create(@Valid @RequestBody InboxCreateRequest req) {
        InboxResponse created = inboxService.create(req);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public InboxResponse update(@PathVariable Long id,
                                @Valid @RequestBody InboxUpdateRequest req) {
        return inboxService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inboxService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/search")
    public List<InboxResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) Inbox.Status status,
            @RequestParam(required = false) Long contextId,
            @RequestParam(required = false) Long projectId) {
        return inboxService.search(keyword, tag, status, contextId, projectId);
    }

}
