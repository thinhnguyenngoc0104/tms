package com.tms.tms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tms.tms.entity.TaskEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Optional<TaskEntity> findById(Long id);
    
    @Query("SELECT t FROM TaskEntity t WHERE t.project.id = :projectId")
    List<TaskEntity> findAllByProjectId(@Param("projectId") Long projectId);
}
