package com.tms.tms.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.tms.tms.entity.UserEntity;
import com.tms.tms.repository.UserRepository;
import com.tms.tms.service.ImpersonationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrentUserProvider {

    private final ImpersonationService impersonationService;
    private final UserRepository userRepository;

    public String getCurrentUserSub() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new IllegalStateException("No authenticated user found");
        }

        // Check if there's an active impersonation session for this admin
        if (auth.getPrincipal() instanceof Jwt jwt) {
            String adminSub = jwt.getClaimAsString("sub");

            return impersonationService.getImpersonatedUserId(adminSub)
                    .flatMap(userRepository::findById)
                    .map(UserEntity::getSub)
                    .orElseGet(() -> {
                        log.debug("Using admin sub {}", adminSub);
                        return adminSub;
                    });
        }
        
        throw new IllegalStateException("Unsupported authentication type: " + auth.getClass());
    }

    public boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            return false;

        // Check if there's an active impersonation session for this admin
        if (auth.getPrincipal() instanceof Jwt jwt) {
            String adminSub = jwt.getClaimAsString("sub");

            return impersonationService.getImpersonatedUserId(adminSub)
                    .flatMap(userRepository::findById)
                    .map(user -> role.equalsIgnoreCase(user.getRole()))
                    .orElseGet(() ->
                            auth.getAuthorities().stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .anyMatch(r -> r.equalsIgnoreCase(role))
                    );
        }

        return false;
    }
}
