package com.ndevstudios.synccanvas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateWorkspaceRequest {
    @NotBlank
    private String name;

    private String description;
}
