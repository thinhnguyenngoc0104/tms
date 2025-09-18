package com.tms.tms.io;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private UserResponse owner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
