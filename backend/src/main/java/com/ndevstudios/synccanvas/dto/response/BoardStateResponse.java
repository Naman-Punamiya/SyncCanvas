package com.ndevstudios.synccanvas.dto.response;

import tools.jackson.databind.JsonNode;
import java.time.LocalDateTime;

public record BoardStateResponse(
        Long boardId,
        JsonNode contentJson,
        Long version,
        LocalDateTime updatedAt
) {
}