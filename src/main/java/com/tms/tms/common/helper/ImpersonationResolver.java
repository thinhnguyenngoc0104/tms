package com.tms.tms.common.helper;

import java.util.Optional;

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
public class ImpersonationResolver {

    private final ImpersonationService impersonationService;
    private final UserRepository userRepository;

    public String resolveSub() {
        Authentication auth = getAuthOrThrow();
        Jwt jwt = getJwtOrThrow(auth);
        return getImpersonatedUser(jwt)
                .map(UserEntity::getSub)
                .orElseGet(() -> {
                    String adminSub = jwt.getClaimAsString("sub");
                    log.info("Using admin sub {}", adminSub);
                    return adminSub;
                });
    }

    public boolean resolveRole(String role) {
        Authentication auth = getAuthOrThrow();
        Jwt jwt = getJwtOrThrow(auth);
        return getImpersonatedUser(jwt)
                .map(user -> role.equalsIgnoreCase(user.getRole()))
                .orElseGet(() -> checkAuthorities(auth, role));
    }

    private Authentication getAuthOrThrow() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            throw new IllegalStateException("No authenticated user");
        return auth;
    }

    private Jwt getJwtOrThrow(Authentication auth) {
        if (auth.getPrincipal() instanceof Jwt jwt)
            return jwt;
        throw new IllegalStateException("Unsupported authentication: " + auth.getClass());
    }

    private boolean checkAuthorities(Authentication auth, String role) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(r -> r.equalsIgnoreCase(role));
    }

    private Optional<UserEntity> getImpersonatedUser(Jwt jwt) {
        String adminSub = jwt.getClaimAsString("sub");
        return impersonationService.getImpersonatedUserId(adminSub)
                .flatMap(userRepository::findById);
    }
}
