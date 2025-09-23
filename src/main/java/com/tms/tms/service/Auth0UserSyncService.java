package com.tms.tms.service;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.tms.tms.entity.UserEntity;
import com.tms.tms.io.UserResponse;
import com.tms.tms.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class Auth0UserSyncService {

    private final UserRepository userRepository;

    public void syncUserFromJwt(Jwt jwt) {
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

        // Lưu DTO vào SecurityContext
        UserResponse userDto = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .sub(user.getSub())
                .role(user.getRole())
                .build();

        log.info("DTO: {}", userDto.toString());

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDto, jwt,
                List.of(new SimpleGrantedAuthority(user.getRole())));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
