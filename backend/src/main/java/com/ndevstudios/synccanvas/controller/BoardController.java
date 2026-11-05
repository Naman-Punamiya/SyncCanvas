package com.ndevstudios.synccanvas.controller;

import com.ndevstudios.synccanvas.dto.request.CreateBoardRequest;
import com.ndevstudios.synccanvas.dto.request.UpdateBoardRequest;
import com.ndevstudios.synccanvas.dto.response.BoardResponse;
import com.ndevstudios.synccanvas.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces/{workspaceId}/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(@PathVariable Long workspaceId, @Valid @RequestBody CreateBoardRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.createBoard(workspaceId, req));
    }

    @GetMapping
    public ResponseEntity<List<BoardResponse>> getBoards(@PathVariable Long workspaceId) {
        return ResponseEntity.ok(boardService.getBoards(workspaceId));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Long workspaceId, @PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getBoard(workspaceId, boardId));
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResponse> updateBoard(@PathVariable Long workspaceId, @PathVariable Long boardId, @Valid @RequestBody UpdateBoardRequest request) {
        return ResponseEntity.ok(boardService.updateBoard(workspaceId, boardId, request));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long workspaceId, @PathVariable Long boardId) {
        boardService.deleteBoard(workspaceId, boardId);
        return ResponseEntity.noContent().build();
    }
}
