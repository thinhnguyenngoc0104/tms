package com.tms.tms.common.mapper;

import com.tms.tms.entity.ProjectEntity;
import com.tms.tms.io.ProjectRequest;
import com.tms.tms.io.ProjectResponse;
import com.tms.tms.io.UserResponse;

public class ProjectMapper {
    public static ProjectResponse toResponse(ProjectEntity project) {
        UserResponse owner = null;
        if (project.getOwner() != null) {
            owner = UserResponse.builder()
                    .id(project.getOwner().getId())
                    .name(project.getOwner().getName())
                    .email(project.getOwner().getEmail())
                    .build();
        }

        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .ownerId(project.getOwner() != null ? project.getOwner().getId() : null)
                .owner(owner)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    public static ProjectEntity toEntity(ProjectRequest request) {
        return ProjectEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }
}
