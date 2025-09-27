package com.tms.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tms.tms.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findBySub(String sub);

    Optional<UserEntity> findById(Long id);

    @Query("SELECT u FROM ProjectEntity p JOIN p.members u WHERE p.id = :projectId")
    List<UserEntity> findAllMembersByProjectId(@Param("projectId") Long projectId);
}
