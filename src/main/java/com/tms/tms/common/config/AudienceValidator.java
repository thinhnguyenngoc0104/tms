package com.tms.tms.common.config;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

import java.util.List;

public class AudienceValidator implements OAuth2TokenValidator<Jwt> {
    private final List<String> expectedAudiences;

    public AudienceValidator(List<String> expectedAudiences) {
        Assert.notEmpty(expectedAudiences , "expectedAudiences cannot be null or empty");
        this.expectedAudiences  = expectedAudiences ;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        List<String> audiences = jwt.getAudience();
        
        if (audiences != null) {
            for (String expected : expectedAudiences) {
                if (audiences.contains(expected)) {
                    return OAuth2TokenValidatorResult.success();
                }
            }
        }
        
        return OAuth2TokenValidatorResult.failure(new OAuth2Error(
            "invalid_audience", 
            "The required audience is missing. Expected one of: " + expectedAudiences,
            null
        ));
    }
}
