package com.shkvlnc.bujo_app.web;

import com.shkvlnc.bujo_app.domain.Tag;
import com.shkvlnc.bujo_app.domain.Inbox;
import com.shkvlnc.bujo_app.dto.InboxResponse;
import com.shkvlnc.bujo_app.dto.TagResponse;
import com.shkvlnc.bujo_app.repository.TagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagRepository tagRepo;

    public TagController(TagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    // ✅ GET /api/tags → list all tags
    @GetMapping
    public List<TagResponse> listAllTags() {
        return tagRepo.findAll().stream()
                .map(TagResponse::new)
                .toList();
    }

    // ✅ GET /api/tags/{id}/inboxes → list inbox items linked to a tag
    @GetMapping("/search")
    public List<InboxResponse> listInboxesByTagName(@RequestParam String name) {
        Tag tag = tagRepo.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));

        return tag.getInboxes().stream()
                .map(InboxResponse::new)
                .toList();
    }
}