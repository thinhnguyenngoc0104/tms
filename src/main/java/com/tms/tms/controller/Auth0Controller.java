package com.tms.tms.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tms.tms.service.Auth0UserSyncService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class Auth0Controller {

    private final Auth0UserSyncService userSyncService;

    @PostMapping("/sync")
    public ResponseEntity<Void> syncUser(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        userSyncService.syncUserFromJwt(jwt);
        return ResponseEntity.noContent().build(); // 204
    }
}
