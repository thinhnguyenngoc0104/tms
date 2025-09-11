package com.tms.tms.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectUpdateRequest {
    private String projectId;
    private String name;
    private String description;
    private String ownerId;
}
