package com.shkvlnc.bujo_app.dto.tag;

import com.shkvlnc.bujo_app.domain.Tag;
import lombok.Getter;

@Getter
public class TagResponse {
    private final Long id;
    private final String name;

    private TagResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TagResponse fromEntity(Tag tag) {
        if (tag == null) {
            return null; // ✅ null-safe
        }
        return new TagResponse(tag.getId(), tag.getName());
    }
}
