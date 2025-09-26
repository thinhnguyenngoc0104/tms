package com.tms.tms.security;

import org.springframework.stereotype.Component;

import com.tms.tms.common.helper.ImpersonationResolver;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final ImpersonationResolver resolver;

    public String getCurrentUserSub() {
        return resolver.resolveSub();
    }

    public boolean hasRole(String role) {
        return resolver.resolveRole(role);
    }
}
