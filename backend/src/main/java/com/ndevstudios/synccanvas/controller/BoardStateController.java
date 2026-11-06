package com.ndevstudios.synccanvas.controller;

import com.ndevstudios.synccanvas.dto.request.SaveBoardStateRequest;
import com.ndevstudios.synccanvas.dto.response.BoardStateResponse;
import com.ndevstudios.synccanvas.service.BoardStateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workspaces/{workspaceId}/boards/{boardId}/state")
@RequiredArgsConstructor
public class BoardStateController {

    private final BoardStateService boardStateService;

    @GetMapping
    public ResponseEntity<BoardStateResponse> getBoardState(@PathVariable Long workspaceId, @PathVariable Long boardId) {
        return ResponseEntity.ok(boardStateService.getBoardState(workspaceId, boardId));
    }

    @PutMapping
    public ResponseEntity<BoardStateResponse> saveBoardState(@PathVariable Long workspaceId, @PathVariable Long boardId, @Valid @RequestBody SaveBoardStateRequest request) {
        return ResponseEntity.ok(boardStateService.saveBoardState(workspaceId, boardId, request));
    }
}