package com.tms.tms.common.mapper;

import org.springframework.stereotype.Component;

import com.tms.tms.entity.UserEntity;
import com.tms.tms.io.UserRequest;
import com.tms.tms.io.UserResponse;

@Component
public class UserMapper {
    public UserResponse toResponse(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public UserEntity toEntity(UserRequest request) {
        return UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
    }
}
