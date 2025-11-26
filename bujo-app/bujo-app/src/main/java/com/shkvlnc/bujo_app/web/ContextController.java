package com.shkvlnc.bujo_app.web;

import com.shkvlnc.bujo_app.dto.ContextResponse;
import com.shkvlnc.bujo_app.dto.ContextCreateRequest;
import com.shkvlnc.bujo_app.dto.ContextUpdateRequest;
import com.shkvlnc.bujo_app.service.ContextService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/contexts")
public class ContextController {
    private final ContextService contextService;

    // ✅ GET all contexts
    @GetMapping
    public List<ContextResponse> list() {
        return contextService.listAll();
    }

    // ✅ GET single context
    @GetMapping("/{id}")
    public ContextResponse getById(@PathVariable Long id) {
        return contextService.getById(id);
    }

    // ✅ POST create context
    @PostMapping
    public ResponseEntity<ContextResponse> create(@Valid @RequestBody ContextCreateRequest req) {
        ContextResponse created = contextService.create(req);
        return ResponseEntity.status(201).body(created); // ✅ 201 Created
    }

    // ✅ PUT update context
    @PutMapping("/{id}")
    public ContextResponse update(@PathVariable Long id,
                                  @Valid @RequestBody ContextUpdateRequest req) {
        return contextService.update(id, req); // ✅ simplified, service throws if not found
    }

    // ✅ DELETE context
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contextService.delete(id); // ✅ service throws if not found
        return ResponseEntity.noContent().build();
    }
}
