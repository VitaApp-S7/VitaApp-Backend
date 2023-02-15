package com.vitaquest.challengeservice.Authentication;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public interface IAuthenticationValidator {
    String getUserId(Authentication authentication) throws IllegalAccessException;
    boolean isAdmin(Authentication authentication) throws IllegalAccessException;
}
