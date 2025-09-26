package com.tms.tms.security;

import org.springframework.stereotype.Component;

import com.tms.tms.common.helper.ImpersonationResolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrentUserProvider {

    private final ImpersonationResolver resolver;

    public String getCurrentUserSub() {
        return resolver.resolveSub();
    }

    public boolean hasRole(String role) {
        return resolver.resolveRole(role);
    }
}
