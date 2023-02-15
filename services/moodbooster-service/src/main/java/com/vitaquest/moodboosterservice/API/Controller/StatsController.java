package com.vitaquest.moodboosterservice.API.Controller;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.vitaquest.moodboosterservice.Domain.Service.StatsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "Moodbooster stats Controller")
@RestController
@CrossOrigin(origins = "*")
@PreAuthorize("hasAuthority('SCOPE_User.All')")
@RequestMapping("stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService service) {
        this.statsService = service;
    }

    @ApiOperation("Get moodbooster stats")
    @GetMapping("/moodboosterusage")
    public @ResponseBody ResponseEntity<?> getusermoodboosterstats() throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authContext)) {
            return new ResponseEntity<>(statsService.getUserMoodboosterStats(), HttpStatus.OK);
        } else
            return new ResponseEntity<>("User should contain an admin role!", HttpStatus.FORBIDDEN);
    }

    private boolean isAdmin(Authentication authContext) throws IllegalAccessException {
        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authContext.getPrincipal(), "claims", true);
        JSONArray arr = (JSONArray) claims.get("roles");
        if (arr != null) {
            String role = (String) arr.get(0);
            return role.equals("Role.Admins");
        } else {
            return false;
        }
    }

}
