package com.vitaquest.moodboosterservice.API.Controller;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.vitaquest.moodboosterservice.Domain.DTO.UpdateMoodboosterDTO;
import com.vitaquest.moodboosterservice.Domain.Models.Moodbooster;

import com.vitaquest.moodboosterservice.Domain.Models.UserMoodbooster;
import com.vitaquest.moodboosterservice.Domain.Models.UserMoodboosterInvite;
import com.vitaquest.moodboosterservice.Domain.Service.MoodboosterService;
import com.vitaquest.moodboosterservice.Domain.DTO.AddMoodboosterDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "Moodbooster Controller")
@RestController
@CrossOrigin(origins = "*")
@PreAuthorize("hasAuthority('SCOPE_User.All')")
public class MoodboosterController {

    private final MoodboosterService service;

    public MoodboosterController(MoodboosterService service)
    {
        this.service = service;
    }

    @ApiOperation(value = "Add moodbooster")
    @PostMapping
    public @ResponseBody ResponseEntity<Moodbooster> addMoodbooster(@RequestBody AddMoodboosterDTO DTO) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authContext)) {
            return new ResponseEntity<>(service.addMoodbooster(DTO), HttpStatus.CREATED);
        }
        else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value = "Update moodbooster")
    @PutMapping("/update")
    public @ResponseBody ResponseEntity<Moodbooster> updateMoodbooster(@RequestBody UpdateMoodboosterDTO DTO) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authContext)) {
            return new ResponseEntity<>(service.updateMoodbooster(DTO), HttpStatus.CREATED);
        }
        else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    @ApiOperation(value = "Get all moodboosters")
    @GetMapping(value = "/all")
    public @ResponseBody ResponseEntity<?> getAllMoodboosters() throws IllegalAccessException
    {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if(isAdmin(authContext))
        {
            return new ResponseEntity<>(service.getAllMoodboosters(), HttpStatus.OK);
        }
        else return new ResponseEntity<>("User should contain an admin role!", HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value = "Get all completed moodboosters")
    @GetMapping(value = "/completed")
    public @ResponseBody List<UserMoodbooster> getAllCompletedMoodboosters() throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.getAllCompletedMoodboosters(authContext);
    }

    @ApiOperation(value = "Get all active moodboosters")
    @GetMapping(value = "/active")
    public @ResponseBody List<Moodbooster> GetAllActiveMoodboosters()
    {
        return service.getAllActiveMoodboosters();
    }

    @ApiOperation(value = "Get all accepted moodboosters")
    @GetMapping(value = "/accepted")
    public @ResponseBody List<UserMoodbooster> GetAllAcceptedMoodboosters() throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.getAllAcceptedMoodboosters(authContext);
    }

    @ApiOperation(value = "Get moodbooster by ID")
    @GetMapping(value = "/{moodboosterId}")
    public @ResponseBody Moodbooster getMoodboosterByID(@PathVariable String moodboosterId) {
        return service.getMoodboosterByID(moodboosterId);
    }

    @ApiOperation(value = "Accept Moodbooster")
    @PutMapping(value = "/{moodboosterId}")
    public @ResponseBody
    UserMoodbooster acceptMoodbooster(@PathVariable String moodboosterId) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.acceptMoodbooster(moodboosterId, authContext);
    }

    @ApiOperation(value = "Cancel Moodbooster")
    @PutMapping(value = "/cancel/{userMoodboosterId}")
    public @ResponseBody boolean cancelMoodbooster(@PathVariable String userMoodboosterId) {
        return service.cancelMoodbooster(userMoodboosterId);
    }

    @ApiOperation(value = "Mark Moodbooster complete")
    @PutMapping(value = "/complete/{userMoodboosterId}")
    public @ResponseBody UserMoodbooster completeMoodbooster(@PathVariable String userMoodboosterId) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        UserMoodbooster moodbooster = service.completeMoodbooster(authContext, userMoodboosterId);
        return moodbooster;
    }

    @ApiOperation(value = "Delete moodbooster by ID")
    @DeleteMapping(value = "/{moodboosterId}")
    public ResponseEntity<HttpStatus> deleteMoodboosterByID(@PathVariable String moodboosterId) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authContext)) {
            service.deleteMoodboosterByID(moodboosterId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value = "Invite user for moodbooster")
    @PostMapping("/invite/{userMoodboosterId}/{invitedUserId}")
    public @ResponseBody ResponseEntity<?> inviteUser(@PathVariable String userMoodboosterId, @PathVariable String invitedUserId) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        try {
            return new ResponseEntity<>(service.inviteUser(userMoodboosterId, invitedUserId, authContext), HttpStatus.OK);
        } catch (IllegalAccessException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Get all moodbooster invites")
    @GetMapping(value = "/invites")
    public @ResponseBody List<UserMoodboosterInvite> getMoodboosterInvites() throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.getInvites(authContext);
    }

    @ApiOperation(value = "accept moodbooster invite")
    @PostMapping(value = "/invite/accept/{inviteId}")
    public @ResponseBody ResponseEntity<?> acceptMoodboosterInvite(@PathVariable String inviteId) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        try {
            UserMoodbooster userMoodbooster = service.acceptMoodboosterInvite(authContext, inviteId);
            return new ResponseEntity<>(userMoodbooster, HttpStatus.ACCEPTED);
        } catch (IllegalAccessException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "decline moodbooster invite")
    @DeleteMapping(value = "/invite/decline/{inviteId}")
    public @ResponseBody ResponseEntity<?> declineMoodboosterInvite(@PathVariable String inviteId) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        try {
            return new ResponseEntity<>(service.declineMoodboosterInvite(authContext, inviteId), HttpStatus.OK);
        } catch (IllegalAccessException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
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
