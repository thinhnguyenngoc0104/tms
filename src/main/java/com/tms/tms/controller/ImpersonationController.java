package com.tms.tms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse startImpersonation(@PathVariable Long userId) {
        return impersonationService.startImpersonation(userId);
    }

    @PostMapping("/stop")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void stopImpersonation() {
        impersonationService.stopImpersonation();
    }
}