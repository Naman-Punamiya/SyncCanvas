package com.ndevstudios.synccanvas.repository;

import com.ndevstudios.synccanvas.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    List<Workspace> findAllByOwner_Id(Long ownerId);
}