package com.tms.tms.io;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponse {
    private String taskId;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;
    private String assigneeId;
    private String projectId;
}
