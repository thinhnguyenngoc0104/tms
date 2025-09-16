package com.tms.tms.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private String sub;        // Auth0 user ID
    private String email;
    private String name;
    private String picture;    // Profile picture URL from Auth0
    private String role;
    private Boolean emailVerified;
}
