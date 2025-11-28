package com.shkvlnc.bujo_app.service;

import com.shkvlnc.bujo_app.domain.Tag;
import com.shkvlnc.bujo_app.dto.inbox.InboxResponse;
import com.shkvlnc.bujo_app.dto.tag.TagCreateRequest;
import com.shkvlnc.bujo_app.dto.tag.TagUpdateRequest;
import com.shkvlnc.bujo_app.dto.tag.TagResponse;
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
                .map(TagResponse::fromEntity)
                .toList();
    }

    public TagResponse getById(Long id) {
        Tag tag = tagRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        return TagResponse.fromEntity(tag);
    }

    public List<TagResponse> createTags(TagCreateRequest req) {
        return req.getNames().stream()
                .map(name -> tagRepo.findByNameIgnoreCase(name)
                        .orElseGet(() -> tagRepo.save(new Tag(name))))
                .map(TagResponse::fromEntity)
                .toList();
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

    public List<InboxResponse> getInboxesByTagId(Long id) {
        Tag tag = tagRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found: " + id));
        return tag.getInboxes().stream()
                .map(InboxResponse::fromEntity)
                .toList();
    }
}
