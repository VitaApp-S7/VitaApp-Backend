package com.vitaquest.badgeservice.Utilities;

import com.nimbusds.jose.shaded.json.JSONArray;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthenticationContextValidator implements IAuthenticationValidator{

    // Moved to different class to allow test stubs.
    public boolean isAdmin(Authentication authContext) throws IllegalAccessException {
        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authContext.getPrincipal(), "claims", true);
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
