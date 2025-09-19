package com.tms.tms.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tms.tms.entity.UserEntity;
import com.tms.tms.io.UserResponse;
import com.tms.tms.service.AuthorizationService;
import com.tms.tms.service.Auth0UserSyncService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class Auth0Controller {

    private final Auth0UserSyncService userSyncService;
    private final AuthorizationService authorizationService;
    private final JwtDecoder jwtDecoder;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile(@RequestHeader("X-ID-Token") String idToken) {
        Jwt jwtId = jwtDecoder.decode(idToken); // decode ID Token
        UserEntity user = userSyncService.syncUserFromJwt(jwtId);

        //TODO: Inject mapper
        UserResponse profile = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(authorizationService.isAdmin() ? "ADMIN" : "USER")
                .pictureUrl(user.getPictureUrl())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        return ResponseEntity.ok(profile);
    }
}
