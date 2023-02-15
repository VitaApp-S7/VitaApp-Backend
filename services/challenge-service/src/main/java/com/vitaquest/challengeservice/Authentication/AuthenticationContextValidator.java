package com.vitaquest.challengeservice.Authentication;

import com.nimbusds.jose.shaded.json.JSONArray;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;


public class AuthenticationContextValidator implements IAuthenticationValidator{

    @Override
    public String getUserId(Authentication authentication) throws IllegalAccessException {
        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authentication.getPrincipal(), "claims", true);
        return claims.get("oid").toString();
    }

    @Override
    public boolean isAdmin(Authentication authentication) throws IllegalAccessException {
        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authentication.getPrincipal(), "claims", true);
        JSONArray arr = (JSONArray) claims.get("roles");
        if (arr != null)
        {
            String role = (String) arr.get(0);
            return role.equals("Role.Admins");
        } else
        {
            return false;
        }
    }
}
