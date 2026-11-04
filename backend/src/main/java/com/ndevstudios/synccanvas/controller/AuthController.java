package com.ndevstudios.synccanvas.controller;

import com.ndevstudios.synccanvas.dto.request.LoginRequest;
import com.ndevstudios.synccanvas.dto.response.LoginResponse;
import com.ndevstudios.synccanvas.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req){
        LoginResponse response = userService.authenticate(req);
        return ResponseEntity.ok(response);
    }
}
