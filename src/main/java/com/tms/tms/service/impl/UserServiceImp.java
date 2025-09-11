package com.tms.tms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tms.tms.entity.UserEntity;
import com.tms.tms.io.UserRequest;
import com.tms.tms.io.UserResponse;
import com.tms.tms.repository.UserRepository;
import com.tms.tms.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponse add(UserRequest request) {
        UserEntity user = toEntity(request);
        user = userRepository.save(user);
        return toResponse(user);
    }

    @Override
    public String getUserRole(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email))
                .getRole();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> read() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse profile(String userId) {
        return userRepository.findByUserId(userId)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    private UserResponse toResponse(UserEntity user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .role(user.getRole())
                .build();
    }

    private UserEntity toEntity(UserRequest request) {
        return UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .build();
    }
}
