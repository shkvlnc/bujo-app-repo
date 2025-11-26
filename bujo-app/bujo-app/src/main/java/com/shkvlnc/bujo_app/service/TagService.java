package com.shkvlnc.bujo_app.service;

import com.shkvlnc.bujo_app.domain.Inbox;
import com.shkvlnc.bujo_app.domain.Tag;
import com.shkvlnc.bujo_app.dto.InboxResponse;
import com.shkvlnc.bujo_app.dto.TagCreateRequest;
import com.shkvlnc.bujo_app.dto.TagUpdateRequest;
import com.shkvlnc.bujo_app.dto.TagResponse;
import com.shkvlnc.bujo_app.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepo;

    public List<TagResponse> listAll() {
        return tagRepo.findAll().stream()
                .map(TagResponse::fromEntity) // ✅ consistent DTO mapping
                .toList();
    }

    public TagResponse getById(Long id) {
        Tag tag = tagRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        return TagResponse.fromEntity(tag);
    }

    public TagResponse create(TagCreateRequest req) {
        if (tagRepo.existsByNameIgnoreCase(req.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tag with this name already exists");
        }
        Tag tag = new Tag();
        tag.setName(req.getName());
        return TagResponse.fromEntity(tagRepo.save(tag));
    }

    public TagResponse update(Long id, TagUpdateRequest req) {
        Tag existing = tagRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        existing.setName(req.getName());
        return TagResponse.fromEntity(tagRepo.save(existing));
    }

    public void delete(Long id) {
        if (!tagRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }
        tagRepo.deleteById(id);
    }

    public List<InboxResponse> getInboxesByTagName(String name) {
        Tag tag = tagRepo.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found: " + name));
        return tag.getInboxes().stream()
                .map(InboxResponse::fromEntity) // ✅ convert entity → DTO
                .toList();
    }


    public List<TagResponse> getTagsByTagName(String name) {
        return tagRepo.findByNameContainingIgnoreCase(name).stream()
                .map(TagResponse::fromEntity)
                .toList();
    }


}
