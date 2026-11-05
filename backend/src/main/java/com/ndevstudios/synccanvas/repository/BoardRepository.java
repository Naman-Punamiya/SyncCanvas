package com.ndevstudios.synccanvas.repository;

import com.ndevstudios.synccanvas.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByWorkspace_Id(Long workspaceId);
}