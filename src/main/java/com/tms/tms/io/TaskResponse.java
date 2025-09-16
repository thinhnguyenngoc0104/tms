package com.tms.tms.io;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private Long assigneeId;
    private UserResponse assignee;
    private Long projectId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
