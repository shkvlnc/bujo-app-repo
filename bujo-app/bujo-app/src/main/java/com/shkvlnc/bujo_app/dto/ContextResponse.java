package com.shkvlnc.bujo_app.dto;

import com.shkvlnc.bujo_app.domain.Context;

public class ContextResponse {
    private Long id;
    private String name;

    public ContextResponse(Context context) {
        this.id = context.getId();
        this.name = context.getName();
    }

    // getters
}