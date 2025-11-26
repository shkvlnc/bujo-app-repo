package com.shkvlnc.bujo_app.web;

import com.shkvlnc.bujo_app.domain.Inbox;
import com.shkvlnc.bujo_app.dto.InboxCreateRequest;
import com.shkvlnc.bujo_app.dto.InboxResponse;
import com.shkvlnc.bujo_app.dto.InboxUpdateRequest;
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
        return inboxService.listAll();
    }

    @GetMapping("/{id}")
    public InboxResponse getById(@PathVariable Long id) {
        return inboxService.getById(id);
    }

    @PostMapping
    public ResponseEntity<InboxResponse> create(@Valid @RequestBody InboxCreateRequest req) {
        InboxResponse created = inboxService.create(req);
        return ResponseEntity.status(201).body(created); // ✅ 201 Created
    }

    @PutMapping("/{id}")
    public InboxResponse update(@PathVariable Long id,
                                @Valid @RequestBody InboxUpdateRequest req) {
        return inboxService.update(id, req); // ✅ simplified, service throws if not found
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inboxService.delete(id); // ✅ service throws if not found
        return ResponseEntity.noContent().build();
    }

    // ✅ Flexible search: keyword, tag, status (enum)
    @GetMapping("/search")
    public List<InboxResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) Inbox.Status status) {
        return inboxService.search(keyword, tag, status);
    }
}
