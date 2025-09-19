package com.tms.tms.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import com.tms.tms.entity.UserEntity;
import com.tms.tms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    // private final Auth0UserSyncService userSyncService;
    private final UserRepository userRepository;

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            return userRepository.findBySub(jwt.getClaimAsString("sub"))
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        throw new RuntimeException("No authenticated user found");
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }
}
