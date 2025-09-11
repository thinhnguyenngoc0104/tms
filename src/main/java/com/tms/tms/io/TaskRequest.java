package com.tms.tms.io;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;
    private String assigneeId;
    private String projectId;
}
