package com.ndevstudios.synccanvas.dto.response;

import java.time.LocalDateTime;

public record BoardResponse(
        Long id,
        String name,
        String description,
        Long workspaceId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
