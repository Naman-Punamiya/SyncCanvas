package com.ndevstudios.synccanvas.dto.request;

import tools.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SaveBoardStateRequest {
    @NotNull(message = "Board content is required")
    private JsonNode contentJson;
}
