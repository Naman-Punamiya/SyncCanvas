package com.ndevstudios.synccanvas.dto.response;

public record LoginResponse(
        String accessToken,
        String tokenType,
        UserResponse user
) {
}
