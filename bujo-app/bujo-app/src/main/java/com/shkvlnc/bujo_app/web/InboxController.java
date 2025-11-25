package com.shkvlnc.bujo_app.web;


import com.shkvlnc.bujo_app.dto.InboxCreateRequest;
import com.shkvlnc.bujo_app.dto.InboxResponse;
import com.shkvlnc.bujo_app.dto.InboxUpdateRequest;
import com.shkvlnc.bujo_app.service.InboxService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inbox")
public class InboxController {
    private final InboxService inboxService;
    public InboxController(InboxService inboxService) { this.inboxService = inboxService; }

    @GetMapping public List<InboxResponse> list() { return inboxService.listAll(); }

    @GetMapping("/status/{status}") public List<InboxResponse> listByStatus(@PathVariable String status) {
        return inboxService.listByStatus(status);
    }

    @GetMapping("/{id}")
    public InboxResponse getById(@PathVariable Long id) {
        return inboxService.getById(id);
    }

    @PostMapping public ResponseEntity<InboxResponse> create(@Valid @RequestBody InboxCreateRequest req) {
        return ResponseEntity.ok(inboxService.create(req));
    }
    @PutMapping("/{id}") public ResponseEntity<InboxResponse> update(@PathVariable Long id,
                                                                     @Valid @RequestBody InboxUpdateRequest req) {
        return ResponseEntity.ok(inboxService.update(id, req));
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) {
        inboxService.delete(id); return ResponseEntity.noContent().build();
    }
}