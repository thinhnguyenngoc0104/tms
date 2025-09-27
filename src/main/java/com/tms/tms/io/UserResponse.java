package com.tms.tms.io;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String sub;
    private String role;
    private String email;
    private String pictureUrl;
}
