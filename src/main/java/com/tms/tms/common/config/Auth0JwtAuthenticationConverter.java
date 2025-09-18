package com.tms.tms.common.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class Auth0JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthority(jwt);
        return new JwtAuthenticationToken(jwt, authorities);
    }

    // Extract roles from Auth0 token
    // In custom claims like "https://tms-api/roles"
    private Collection<GrantedAuthority> extractAuthority(Jwt jwt) {

        // Get roles from custom namespace
        List<String> roles = jwt.getClaimAsStringList("https://tms-api/roles");
        if (roles != null && !roles.isEmpty()) {
            // log.info("Roles found in custom namespace: {}", roles);
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role.toUpperCase()))
                    .collect(Collectors.toList());
        }

        // Default role if no roles found
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }
}
