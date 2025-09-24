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

    public Long syncUserFromJwt(Jwt jwt) {
        log.info("JWT Claims: {}", jwt.getClaims());

        String sub = jwt.getClaimAsString("sub");
        String name = jwt.getClaimAsString("https://tms-api/name");
        String role = jwt.getClaimAsStringList("https://tms-api/roles").get(0);

        UserEntity user = userRepository.findBySub(sub).orElse(null);

        if (user == null) {
            if (sub == null || sub.trim().isEmpty()) {
                throw new IllegalArgumentException("Cannot create user without sub. JWT claims: " + jwt.getClaims());
            }

            user = UserEntity.builder()
                    .name(name)
                    .sub(sub)
                    .role(role)
                    .build();
            userRepository.save(user);
        } else {
            boolean updated = false;

            if (name != null && !name.equals(user.getName())) {
                user.setName(name);
                updated = true;
            }
            if (sub != null && !sub.equals(user.getSub())) {
                user.setSub(sub);
                updated = true;
            }
            if (role != null && !role.equals(user.getRole())) {
                user.setRole(role);
                updated = true;
            }
            if (updated) {
                userRepository.save(user);
            }
        }

        return user.getId();
    }
}
