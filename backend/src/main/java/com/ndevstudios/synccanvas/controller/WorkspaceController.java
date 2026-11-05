package com.ndevstudios.synccanvas.controller;

import com.ndevstudios.synccanvas.dto.request.CreateWorkspaceRequest;
import com.ndevstudios.synccanvas.dto.request.UpdateWorkspaceRequest;
import com.ndevstudios.synccanvas.dto.response.WorkspaceResponse;
import com.ndevstudios.synccanvas.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<WorkspaceResponse> createWorkspace(@Valid @RequestBody CreateWorkspaceRequest req){
        return ResponseEntity.status(HttpStatus.CREATED).body(workspaceService.createWorkspace(req));
    }

    @GetMapping
    public ResponseEntity<List<WorkspaceResponse>> getMyWorkspaces(){
        return ResponseEntity.status(HttpStatus.OK).body(workspaceService.getMyWorkspaces());
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceResponse> getWorkspace(@PathVariable Long workspaceId) {
        return ResponseEntity.status(HttpStatus.OK).body(workspaceService.getWorkspace(workspaceId));
    }

    @PutMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceResponse> updateWorkspace(@PathVariable Long workspaceId, @Valid @RequestBody UpdateWorkspaceRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(workspaceService.updateWorkspace(workspaceId, request));
    }

    @DeleteMapping("/{workspaceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long workspaceId) {
        workspaceService.deleteWorkspace(workspaceId);
        return ResponseEntity.noContent().build();
    }
}
