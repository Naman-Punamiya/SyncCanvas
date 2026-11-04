package com.ndevstudios.synccanvas.service;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    void shouldGenerateAndValidateToken() {

        // Create a test-only secret (32 bytes)
        String testSecret = Base64.getEncoder().encodeToString(
                "01234567890123456789012345678901"
                        .getBytes(StandardCharsets.UTF_8)
        );

        JwtService jwtService = new JwtService(testSecret);

        String userId = "123";

        // Generate token
        String token = jwtService.generateToken(userId);

        // Extract user ID
        String extractedUserId = jwtService.extractUserId(token);

        // Validate token
        boolean valid = jwtService.isTokenValid(token, userId);

        assertNotNull(token);
        assertEquals(userId, extractedUserId);
        assertTrue(valid);
    }

    @Test
    void shouldRejectTokenForDifferentUser() {

        String testSecret = Base64.getEncoder().encodeToString(
                "01234567890123456789012345678901"
                        .getBytes(StandardCharsets.UTF_8)
        );

        JwtService jwtService = new JwtService(testSecret);

        String token = jwtService.generateToken("123");

        assertFalse(jwtService.isTokenValid(token, "456"));
    }
}