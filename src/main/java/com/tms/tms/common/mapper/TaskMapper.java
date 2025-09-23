package com.tms.tms.common.mapper;

import org.springframework.stereotype.Component;

import com.tms.tms.entity.TaskEntity;
import com.tms.tms.io.TaskRequest;
import com.tms.tms.io.TaskResponse;
import com.tms.tms.io.UserResponse;

@Component
public class TaskMapper {
    public TaskResponse toResponse(TaskEntity task) {
        UserResponse assignee = null;
        if (task.getAssignee() != null) {
            assignee = UserResponse.builder()
                    .id(task.getAssignee().getId())
                    .name(task.getAssignee().getName())
                    .build();
        }

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .assigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null)
                .assignee(assignee)
                .projectId(task.getProject().getId())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

    public TaskEntity toEntity(TaskRequest request) {
        return TaskEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .build();
    }

}
