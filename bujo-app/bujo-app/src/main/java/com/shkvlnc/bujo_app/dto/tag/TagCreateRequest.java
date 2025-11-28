package com.shkvlnc.bujo_app.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TagCreateRequest {

    @NotEmpty(message = "At least one tag name is required")
    @Size(max = 10, message = "You can create up to 10 tags at once")
    private List<
            @NotBlank(message = "Tag name cannot be blank")
            @Size(max = 100, message = "Tag name must not exceed 100 characters")
                    String
            > names;
}
