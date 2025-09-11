package com.tms.tms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tms.tms.entity.ProjectEntity;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long>{
    Optional<ProjectEntity> findByProjectId(String projectId);
}
