package com.vitaquest.badgeservice.Utilities;

import org.springframework.security.core.Authentication;

public interface IAuthenticationValidator {

    boolean isAdmin(Authentication authentication) throws IllegalAccessException;
}
