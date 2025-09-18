package com.tms.tms.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.tms.tms.entity.UserEntity;
import com.tms.tms.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class Auth0UserSyncService {

    private final UserRepository userRepository;

    public UserEntity syncUserFromJwt(Jwt jwt) {
        // String auth0Id = jwt.getSubject(); // Auth0 user ID  
        String email = jwt.getClaimAsString("https://tms-api/email");
        String name = jwt.getClaimAsString("https://tms-api/name");

        // Log JWT claims for debugging
        // log.info("JWT Claims: {}", jwt.getClaims());
        // log.info("Auth0 ID: {}, Email: {}, Name: {}", auth0Id, email, name);

        UserEntity user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Cannot create user without email. JWT claims: " + jwt.getClaims());
            }

            user = UserEntity.builder()
                    .name(name != null ? name : "Unknown")
                    .email(email)
                    .build();
            user = userRepository.save(user);
        } else {
            boolean updated = false;

            if (name != null && !name.equals(user.getName()) && !name.equals(email)) {
                user.setName(name);
                updated = true;
            }

            if (updated) {
                user = userRepository.save(user);
            }
        }

        return user;
    }
}
