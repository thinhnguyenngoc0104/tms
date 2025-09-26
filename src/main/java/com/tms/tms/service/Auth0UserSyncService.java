package com.tms.tms.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.tms.tms.common.mapper.AppMapper;
import com.tms.tms.entity.UserEntity;
import com.tms.tms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Auth0UserSyncService {

    private final UserRepository userRepository;
    private final AppMapper appMapper;

    public Long syncUserFromJwt(Jwt jwt) {
        String sub = jwt.getClaimAsString("sub");
        if (sub == null || sub.trim().isEmpty()) {
            throw new IllegalArgumentException("Cannot create user without sub. JWT claims: " + jwt.getClaims());
        }

        UserEntity newUser = UserEntity.builder()
                .sub(sub)
                .name(jwt.getClaimAsString("https://tms-api/name"))
                .role(jwt.getClaimAsStringList("https://tms-api/roles").get(0))
                .email(jwt.getClaimAsString("https://tms-api/email"))
                .pictureUrl(jwt.getClaimAsString("https://tms-api/pictureUrl"))
                .build();

        UserEntity user = userRepository.findBySub(sub)
                .map(existing -> {
                    appMapper.updateUserFromJwt(newUser, existing);
                    return existing;
                })
                .orElse(newUser);

        userRepository.save(user);
        return user.getId();
    }
}
