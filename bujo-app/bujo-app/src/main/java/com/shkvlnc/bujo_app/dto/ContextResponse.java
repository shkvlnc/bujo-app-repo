package com.shkvlnc.bujo_app.dto;

import com.shkvlnc.bujo_app.domain.Context;
import lombok.Getter;

@Getter
public class ContextResponse {
    private final Long id;
    private final String name;

    private ContextResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ContextResponse fromEntity(Context context) {
        if (context == null) {
            return null; // ✅ null-safe
        }
        return new ContextResponse(context.getId(), context.getName());
    }
}
