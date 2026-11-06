package com.ndevstudios.synccanvas.repository;

import com.ndevstudios.synccanvas.entity.BoardState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardStateRepository extends JpaRepository<BoardState, Long> {
    Optional<BoardState> findByBoard_Id(Long boardId);
}