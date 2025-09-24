package com.tms.tms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tms.tms.entity.ProjectEntity;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Optional<ProjectEntity> findById(Long id);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM ProjectEntity p " +
            "LEFT JOIN p.members m " +
            "WHERE p.id = :projectId " +
            "AND (p.owner.sub = :sub OR m.sub = :sub)")
    boolean existsByIdAndUserHasAccess(@Param("projectId") Long projectId, @Param("sub") String sub);

    @Query("SELECT DISTINCT p FROM ProjectEntity p " +
            "LEFT JOIN p.members m " +
            "WHERE p.owner.sub = :sub OR m.sub = :sub")
    List<ProjectEntity> findAllByUserAccess(@Param("sub") String sub);
}
