package com.tms.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tms.tms.entity.UserEntity;
import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUserId(String userId);
}
