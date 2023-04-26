package com.vitaquest.challengeservice.Api.controller;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.vitaquest.challengeservice.Domain.DTO.CreateChallengeDTO;
import com.vitaquest.challengeservice.Domain.DTO.CreateTeamDTO;
import com.vitaquest.challengeservice.Domain.DTO.UpdateChallengeDTO;
import com.vitaquest.challengeservice.Domain.DTO.UpdateTeamDTO;
import com.vitaquest.challengeservice.Domain.Models.Challenge;
import com.vitaquest.challengeservice.Domain.Models.Team;
import com.vitaquest.challengeservice.Domain.Service.ChallengeService;
import com.vitaquest.challengeservice.Domain.Service.TeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "Challenge Controller")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAuthority('SCOPE_User.All')")
@RestController
@RequestMapping("team")
public class TeamController {

    private final TeamService service;

    public TeamController(TeamService service){
        this.service = service;
    }

    @ApiOperation(value = "Create a new challenge")
    @PostMapping(value = "/")
    public ResponseEntity<Team> create(@RequestBody CreateTeamDTO dto) throws IllegalAccessException{
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        if(isAdmin(authContext)){
            return new ResponseEntity<>(service.create(dto), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ApiOperation(value = "Get single team")
    @GetMapping("/{id}")
    public ResponseEntity<Team> read(@PathVariable String id){
        return new ResponseEntity<>(service.read(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all teams for a challenge")
    @GetMapping("/challenge/{challengeId}}")
    public ResponseEntity<List<Team>> readByChallenge(@PathVariable String challengeId){
        return new ResponseEntity<>(service.readByChallengeId(challengeId), HttpStatus.OK);
    }

    @ApiOperation(value = "Update a team")
    @PutMapping("/")
    public ResponseEntity<Team> update(@RequestBody UpdateTeamDTO dto) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        if(isAdmin(authContext)){
            return new ResponseEntity<>(service.update(dto), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ApiOperation(value = "Delete a team")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        if(isAdmin(authContext)){
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @ApiOperation(value = "Join a team")
    @PostMapping("/{id}/join")
    public ResponseEntity<Void> join(@PathVariable String id) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        service.join(authContext, id);

        return new ResponseEntity<>(HttpStatus.OK);
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
