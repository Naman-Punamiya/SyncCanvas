package com.ndevstudios.synccanvas.controller;

import com.ndevstudios.synccanvas.dto.request.CreateUserRequest;
import com.ndevstudios.synccanvas.dto.response.UserResponse;
import com.ndevstudios.synccanvas.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers(){
       return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest req){
        UserResponse response = userService.createUser(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
