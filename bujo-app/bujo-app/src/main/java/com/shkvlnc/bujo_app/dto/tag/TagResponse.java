package com.shkvlnc.bujo_app.dto.tag;

import com.shkvlnc.bujo_app.domain.Tag;
import lombok.Getter;

@Getter
public class TagResponse {
    private final Long id;
    private final String name;

    public TagResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TagResponse fromEntity(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }
}

