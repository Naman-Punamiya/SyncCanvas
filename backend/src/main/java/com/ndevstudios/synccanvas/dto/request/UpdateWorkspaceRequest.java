package com.ndevstudios.synccanvas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateWorkspaceRequest {
    @NotBlank
    private String name;

    private String description;
}
