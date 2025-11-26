package com.shkvlnc.bujo_app.web;

import com.shkvlnc.bujo_app.dto.TagCreateRequest;
import com.shkvlnc.bujo_app.dto.TagUpdateRequest;
import com.shkvlnc.bujo_app.dto.TagResponse;
import com.shkvlnc.bujo_app.dto.InboxResponse;
import com.shkvlnc.bujo_app.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    // ✅ GET all tags
    @GetMapping
    public List<TagResponse> listAll() {
        return tagService.listAll();
    }

    // ✅ GET single tag
    @GetMapping("/{id}")
    public TagResponse getById(@PathVariable Long id) {
        return tagService.getById(id);
    }

    // ✅ POST create tag
    @PostMapping
    public ResponseEntity<TagResponse> create(@Valid @RequestBody TagCreateRequest req) {
        TagResponse created = tagService.create(req);
        return ResponseEntity.status(201).body(created);
    }

    // ✅ PUT update tag
    @PutMapping("/{id}")
    public TagResponse update(@PathVariable Long id,
                              @Valid @RequestBody TagUpdateRequest req) {
        return tagService.update(id, req);
    }

    // ✅ DELETE tag
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ GET inboxes linked to a tag by name
    @GetMapping("/{name}/inboxes")
    public List<InboxResponse> listInboxesByTagName(@PathVariable String name) {
        return tagService.getInboxesByTagName(name);
    }
}
