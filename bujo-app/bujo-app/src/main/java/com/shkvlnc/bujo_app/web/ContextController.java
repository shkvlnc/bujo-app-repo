package com.shkvlnc.bujo_app.web;

import com.shkvlnc.bujo_app.dto.context.ContextResponse;
import com.shkvlnc.bujo_app.dto.context.ContextCreateRequest;
import com.shkvlnc.bujo_app.dto.context.ContextUpdateRequest;
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

    @GetMapping
    public List<ContextResponse> listAll() {
        return contextService.listAll();
    }

    @GetMapping("/{id}")
    public ContextResponse getById(@PathVariable Long id) {
        return contextService.getById(id);
    }

    @PostMapping
    public ResponseEntity<ContextResponse> create(@Valid @RequestBody ContextCreateRequest req) {
        ContextResponse created = contextService.create(req);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ContextResponse update(@PathVariable Long id,
                                  @Valid @RequestBody ContextUpdateRequest req) {
        return contextService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contextService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
