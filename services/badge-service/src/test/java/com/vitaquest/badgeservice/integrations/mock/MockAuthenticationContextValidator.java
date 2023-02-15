package com.vitaquest.badgeservice.integrations.mock;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Profile(value = "test")
public class MockAuthenticationContextValidator {


    public boolean isAdmin(Authentication authentication) throws IllegalAccessException {
        if(authentication.isAuthenticated()){
            return true;
        }
        return false;
    }
}
