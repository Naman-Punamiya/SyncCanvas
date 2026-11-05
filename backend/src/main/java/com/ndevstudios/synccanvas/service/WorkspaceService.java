package com.ndevstudios.synccanvas.service;

import com.ndevstudios.synccanvas.dto.request.CreateWorkspaceRequest;
import com.ndevstudios.synccanvas.dto.request.UpdateWorkspaceRequest;
import com.ndevstudios.synccanvas.dto.response.WorkspaceResponse;
import com.ndevstudios.synccanvas.entity.User;
import com.ndevstudios.synccanvas.entity.Workspace;
import com.ndevstudios.synccanvas.exception.WorkspaceAccessDeniedException;
import com.ndevstudios.synccanvas.exception.WorkspaceNotFoundException;
import com.ndevstudios.synccanvas.repository.UserRepository;
import com.ndevstudios.synccanvas.repository.WorkspaceRepository;
import com.ndevstudios.synccanvas.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    public WorkspaceResponse createWorkspace(CreateWorkspaceRequest req){
        Long userId = currentUser.getId();

        User owner = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not Found"));

        Workspace workspace = new Workspace();

        workspace.setName(req.getName());
        workspace.setDescription(req.getDescription());
        workspace.setOwner(owner);

        LocalDateTime now = LocalDateTime.now();
        workspace.setCreatedAt(now);
        workspace.setUpdatedAt(now);

        Workspace savedWorkspace = workspaceRepository.save(workspace);

        return new WorkspaceResponse(
                savedWorkspace.getId(),
                savedWorkspace.getName(),
                savedWorkspace.getDescription(),
                savedWorkspace.getOwner().getId(),
                savedWorkspace.getCreatedAt(),
                savedWorkspace.getUpdatedAt()
        );
    }

    public List<WorkspaceResponse> getMyWorkspaces(){
        Long userId = currentUser.getId();

        List<Workspace> workspaces = workspaceRepository.findAllByOwner_Id(userId);

        return workspaces.stream().map(workspace -> new WorkspaceResponse(
                workspace.getId(),
                workspace.getName(),
                workspace.getDescription(),
                workspace.getOwner().getId(),
                workspace.getCreatedAt(),
                workspace.getUpdatedAt()
        )).toList();
    }

    public WorkspaceResponse getWorkspace(Long workspaceId){
        Long userId = currentUser.getId();
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found"));
        if(!workspace.getOwner().getId().equals(userId)){
            throw new WorkspaceAccessDeniedException("You do not have access to this workspace");
        }
        return new WorkspaceResponse(
                workspace.getId(),
                workspace.getName(),
                workspace.getDescription(),
                workspace.getOwner().getId(),
                workspace.getCreatedAt(),
                workspace.getUpdatedAt()
        );
    }

    public WorkspaceResponse updateWorkspace(Long workspaceId, UpdateWorkspaceRequest req){
        Long userId = currentUser.getId();
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(()->new WorkspaceNotFoundException("Workspace not found"));
        if(!workspace.getOwner().getId().equals(userId)){
            throw new WorkspaceAccessDeniedException("You do not have access to this workspace");
        }
        workspace.setName(req.getName());
        workspace.setDescription(req.getDescription());
        workspace.setUpdatedAt(LocalDateTime.now());
        Workspace updatedWorkspace = workspaceRepository.save(workspace);
        return new WorkspaceResponse(
                updatedWorkspace.getId(),
                updatedWorkspace.getName(),
                updatedWorkspace.getDescription(),
                updatedWorkspace.getOwner().getId(),
                updatedWorkspace.getCreatedAt(),
                updatedWorkspace.getUpdatedAt()
        );
    }

    public void deleteWorkspace(Long workspaceId){
        Long userId = currentUser.getId();
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(()->new WorkspaceNotFoundException("Workspace not found"));
        if(!workspace.getOwner().getId().equals(userId)){
            throw new WorkspaceAccessDeniedException("You do not have access to this workspace");
        }
        workspaceRepository.delete(workspace);
    }
}