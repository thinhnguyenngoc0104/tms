package com.tms.tms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tms.tms.io.UserResponse;
import com.tms.tms.service.ImpersonationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/impersonation")
@RequiredArgsConstructor
public class ImpersonationController {

    private final ImpersonationService impersonationService;

    @PostMapping("/start/{userId}")
    public UserResponse startImpersonation(@PathVariable Long userId) {
        UserResponse res = impersonationService.startImpersonation(userId);
        return res;
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopImpersonation() {
        impersonationService.stopImpersonation();
        return ResponseEntity.ok("Impersonation stopped");
    }
}