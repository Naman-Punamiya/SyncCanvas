package com.ndevstudios.synccanvas.service;

import com.ndevstudios.synccanvas.dto.request.CreateBoardRequest;
import com.ndevstudios.synccanvas.dto.request.UpdateBoardRequest;
import com.ndevstudios.synccanvas.dto.response.BoardResponse;
import com.ndevstudios.synccanvas.entity.Board;
import com.ndevstudios.synccanvas.entity.BoardState;
import com.ndevstudios.synccanvas.entity.Workspace;
import com.ndevstudios.synccanvas.exception.BoardNotFoundException;
import com.ndevstudios.synccanvas.exception.WorkspaceAccessDeniedException;
import com.ndevstudios.synccanvas.exception.WorkspaceNotFoundException;
import com.ndevstudios.synccanvas.repository.BoardRepository;
import com.ndevstudios.synccanvas.repository.BoardStateRepository;
import com.ndevstudios.synccanvas.repository.UserRepository;
import com.ndevstudios.synccanvas.repository.WorkspaceRepository;
import com.ndevstudios.synccanvas.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardStateRepository boardStateRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    @Transactional
    public BoardResponse createBoard(Long workspaceId, CreateBoardRequest req) {
        Long userId = currentUser.getId();

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found"));

        if (!workspace.getOwner().getId().equals(userId)) {
            throw new WorkspaceAccessDeniedException("You dont have access to this workspace");
        }

        Board board = new Board();
        board.setName(req.getName());
        board.setDescription(req.getDescription());
        board.setWorkspace(workspace);

        LocalDateTime now = LocalDateTime.now();
        board.setCreatedAt(now);
        board.setUpdatedAt(now);

        Board savedBoard = boardRepository.save(board);
        BoardState boardState = new BoardState();
        boardState.setBoard(savedBoard);
        boardState.setContentJson(JsonNodeFactory.instance.objectNode());
        boardState.setVersion(0L);
        boardState.setUpdatedAt(now);

        boardStateRepository.save(boardState);

        return new BoardResponse(savedBoard.getId(), savedBoard.getName(), savedBoard.getDescription(), savedBoard.getWorkspace().getId(), savedBoard.getCreatedAt(), savedBoard.getUpdatedAt());
    }

    public List<BoardResponse> getBoards(Long workspaceId) {
        Long userId = currentUser.getId();
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found"));
        if (!workspace.getOwner().getId().equals(userId)) {
            throw new WorkspaceAccessDeniedException("You do not have access to this workspace");
        }
        return boardRepository.findAllByWorkspace_Id(workspaceId).stream().map(board -> new BoardResponse(board.getId(), board.getName(), board.getDescription(), board.getWorkspace().getId(), board.getCreatedAt(), board.getUpdatedAt())).toList();
    }

    public BoardResponse getBoard(Long workspaceId, Long boardId) {
        Long userId = currentUser.getId();
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found"));
        if (!workspace.getOwner().getId().equals(userId)) {
            throw new WorkspaceAccessDeniedException("You do not have access to this workspace");
        }
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("Board not found"));
        if (!board.getWorkspace().getId().equals(workspaceId)) {
            throw new BoardNotFoundException("Board not found in this workspace");
        }
        return new BoardResponse(board.getId(), board.getName(), board.getDescription(), workspaceId, board.getCreatedAt(), board.getUpdatedAt());
    }

    public BoardResponse updateBoard(Long workspaceId, Long boardId, UpdateBoardRequest request) {
        Long userId = currentUser.getId();
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found"));
        if (!workspace.getOwner().getId().equals(userId)) {
            throw new WorkspaceAccessDeniedException("You do not have access to this workspace");
        }

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("Board not found"));
        if (!board.getWorkspace().getId().equals(workspaceId)) {
            throw new BoardNotFoundException("Board not found in this workspace");
        }

        board.setName(request.getName());
        board.setDescription(request.getDescription());
        board.setUpdatedAt(LocalDateTime.now());
        Board updatedBoard = boardRepository.save(board);
        return new BoardResponse(updatedBoard.getId(), updatedBoard.getName(), updatedBoard.getDescription(), workspaceId, updatedBoard.getCreatedAt(), updatedBoard.getUpdatedAt());
    }

    public void deleteBoard(Long workspaceId, Long boardId) {
        Long userId = currentUser.getId();
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found"));
        if (!workspace.getOwner().getId().equals(userId)) {
            throw new WorkspaceAccessDeniedException("You do not have access to this workspace");
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("Board not found"));
        if (!board.getWorkspace().getId().equals(workspaceId)) {
            throw new BoardNotFoundException("Board not found in this workspace");
        }

        boardRepository.delete(board);
    }
}
