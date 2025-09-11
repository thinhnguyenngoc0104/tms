package com.tms.tms.io;

import java.security.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String userId;
    private String name;
    private String email;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String role;
}
