
package com.ndevstudios.synccanvas.service;

import com.ndevstudios.synccanvas.dto.request.SaveBoardStateRequest;
import com.ndevstudios.synccanvas.dto.response.BoardStateResponse;
import com.ndevstudios.synccanvas.entity.Board;
import com.ndevstudios.synccanvas.entity.BoardState;
import com.ndevstudios.synccanvas.exception.BoardNotFoundException;
import com.ndevstudios.synccanvas.exception.BoardStateNotFoundException;
import com.ndevstudios.synccanvas.exception.WorkspaceAccessDeniedException;
import com.ndevstudios.synccanvas.repository.BoardRepository;
import com.ndevstudios.synccanvas.repository.BoardStateRepository;
import com.ndevstudios.synccanvas.security.CurrentUser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardStateService {

    // Jackson 2: Used for Hibernate JSONB persistence
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper =
            new com.fasterxml.jackson.databind.ObjectMapper();

    // Jackson 3: Used for HTTP response serialization
    private final tools.jackson.databind.ObjectMapper responseObjectMapper;

    private final BoardStateRepository boardStateRepository;
    private final BoardRepository boardRepository;
    private final CurrentUser currentUser;

    private tools.jackson.databind.JsonNode convertToResponseJson(
            com.fasterxml.jackson.databind.JsonNode content
    ) {
        return responseObjectMapper.readTree(content.toString());
    }

    public BoardStateResponse getBoardState(Long workspaceId, Long boardId) {

        Long userId = currentUser.getId();

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("Board not found"));

        if (!board.getWorkspace().getId().equals(workspaceId)) {
            throw new BoardNotFoundException("Board not found in this workspace");
        }

        if (!board.getWorkspace().getOwner().getId().equals(userId)) {
            throw new WorkspaceAccessDeniedException(
                    "You do not have access to this workspace"
            );
        }

        BoardState state = boardStateRepository.findByBoard_Id(boardId)
                .orElseThrow(() -> new BoardStateNotFoundException("Board state not found"));

        return new BoardStateResponse(
                boardId,
                convertToResponseJson(state.getContentJson()),
                state.getVersion(),
                state.getUpdatedAt()
        );
    }

    @Transactional
    public BoardStateResponse saveBoardState(
            Long workspaceId,
            Long boardId,
            SaveBoardStateRequest req
    ) {

        Long userId = currentUser.getId();

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("Board not found"));

        if (!board.getWorkspace().getId().equals(workspaceId)) {
            throw new BoardNotFoundException("Board not found in this workspace");
        }

        if (!board.getWorkspace().getOwner().getId().equals(userId)) {
            throw new WorkspaceAccessDeniedException(
                    "You do not have access to this workspace"
            );
        }

        BoardState state = boardStateRepository.findByBoard_Id(boardId)
                .orElseThrow(() -> new BoardStateNotFoundException("Board state not found"));

        try {
            String json = req.getContentJson().toString();
            state.setContentJson(objectMapper.readTree(json));
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid board content", e);
        }

        state.setVersion(state.getVersion() + 1);
        state.setUpdatedAt(LocalDateTime.now());

        BoardState updatedState = boardStateRepository.save(state);

        return new BoardStateResponse(
                boardId,
                convertToResponseJson(updatedState.getContentJson()),
                updatedState.getVersion(),
                updatedState.getUpdatedAt()
        );
    }
}
