package com.shkvlnc.bujo_app.web;

import com.shkvlnc.bujo_app.dto.tag.TagCreateRequest;
import com.shkvlnc.bujo_app.dto.tag.TagUpdateRequest;
import com.shkvlnc.bujo_app.dto.tag.TagResponse;
import com.shkvlnc.bujo_app.dto.inbox.InboxResponse;
import com.shkvlnc.bujo_app.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<TagResponse>> create(@Valid @RequestBody TagCreateRequest req) {
        List<TagResponse> created = tagService.createTags(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
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
    @GetMapping("/{id}/inboxes")
    public List<InboxResponse> listInboxesByTagId(@PathVariable Long id) {
        return tagService.getInboxesByTagId(id);
    }
}
