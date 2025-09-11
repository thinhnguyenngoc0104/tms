package com.tms.tms.io;

import java.security.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectResponse {
    private String projectId;
    private String name;
    private String description;
    private String ownerId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer taskCount;
    private Integer memberCount;
}
