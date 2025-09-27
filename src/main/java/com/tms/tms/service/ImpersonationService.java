package com.tms.tms.service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.tms.tms.common.mapper.AppMapper;
import com.tms.tms.entity.UserEntity;
import com.tms.tms.io.UserResponse;
import com.tms.tms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImpersonationService {

    private final UserRepository userRepository;
    private final AppMapper appMapper;

    // Store active impersonation sessions in memory: adminSub -> impersonatedUserId
    private final Map<String, Long> activeImpersonations = new ConcurrentHashMap<>();

    public UserResponse startImpersonation(Long userId) {
        UserEntity user = getUserOrThrow(userId);
        String adminSub = getSubFromAuth();

        // Store the impersonation session
        activeImpersonations.put(adminSub, userId);

        return appMapper.toUserResponse(user);
    }

    public void stopImpersonation() {
        String adminSub = getSubFromAuth();

        // Remove the impersonation session
        activeImpersonations.remove(adminSub);
    }

    public Optional<Long> getImpersonatedUserId(String adminSub) {
        return Optional.ofNullable(activeImpersonations.get(adminSub));
    }

    private String getSubFromAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("sub");
        }
        throw new IllegalStateException("Cannot extract sub from authentication: " + auth.getClass());
    }

    private UserEntity getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }
}
