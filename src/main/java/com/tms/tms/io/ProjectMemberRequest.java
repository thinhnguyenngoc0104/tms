package com.tms.tms.io;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberRequest {
    
    @NotNull(message = "User ID is required")
    private Long userId;
}
