package com.shkvlnc.bujo_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContextCreateRequest {
    @NotBlank(message = "Context name is required")
    @Size(max = 100, message = "Context name must not exceed 100 characters")
    private String name;
}
