package com.shkvlnc.bujo_app.dto;

import com.shkvlnc.bujo_app.domain.Tag;
import lombok.Data;

@Data
public class TagResponse {
    private Long id;
    private String name;

    public TagResponse(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }
}