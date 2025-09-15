package com.tms.tms.io;

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
    private String assigneeId;
    private String projectId;
}
