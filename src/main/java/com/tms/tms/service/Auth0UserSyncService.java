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
        String email = jwt.getClaimAsString("email");
        String nickname = jwt.getClaimAsString("nickname");
        String sub = jwt.getClaimAsString("sub");
        String pictureUrl = jwt.getClaimAsString("picture");
        String name = jwt.getClaimAsString("name");
        String userName = name == email ? nickname : name;

        log.info("JWT Claims: {}", jwt.getClaims());

        UserEntity user = userRepository.findBySub(sub).orElse(null);

        if (user == null) {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Cannot create user without email. JWT claims: " + jwt.getClaims());
            }

            user = UserEntity.builder()
                    .name(userName)
                    .email(email)
                    .sub(sub)
                    .pictureUrl(pictureUrl)
                    .build();
            user = userRepository.save(user);
        } else {
            boolean updated = false;

            if (!userName.equals(user.getName()) && userName != null) {
                user.setName(userName);
                updated = true;
            }

            if (!pictureUrl.equals(user.getPictureUrl()) && pictureUrl != null) {
                user.setPictureUrl(pictureUrl);
                updated = true;
            }

            if (!sub.equals(user.getSub()) && sub != null) {
                user.setSub(sub);
                updated = true;
            }

            if (updated) {
                user = userRepository.save(user);
            }
        }

        return user;
    }
}
