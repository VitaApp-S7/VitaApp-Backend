package com.vitaquest.challengeservice.Api.controller;

import com.google.common.collect.Lists;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.vitaquest.challengeservice.Domain.DTO.CreateChallengeDTO;
import com.vitaquest.challengeservice.Domain.DTO.UpdateChallengeDTO;
import com.vitaquest.challengeservice.Domain.Models.Challenge;
import com.vitaquest.challengeservice.Domain.Service.ChallengeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kotlin.Unit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Api(tags = "Challenge Controller")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAuthority('SCOPE_User.All')")
@RestController
@RequestMapping("challenge")
public class ChallengeController {

    private final ChallengeService service;

    public ChallengeController(ChallengeService service){
        this.service = service;
    }

    @ApiOperation(value = "Create a new challenge")
    @PostMapping(value = "/")
    public ResponseEntity<Challenge> create(@RequestBody CreateChallengeDTO dto) throws Exception {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        if(isAdmin(authContext)){
            return new ResponseEntity<>(service.create(dto), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ApiOperation(value = "Get single challenge")
    @GetMapping("/{id}")
    public ResponseEntity<Challenge> read(@PathVariable String id){
        return new ResponseEntity<>(service.read(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get active challenge")
    @GetMapping("/active")
    public ResponseEntity<Challenge> readActive(){
        return new ResponseEntity<>(service.readCurrentActive(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all challenges")
    @GetMapping("/all")
    public ResponseEntity<List<Challenge>> readAll(){
        return new ResponseEntity<>(service.readAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Update a challenge")
    @PutMapping("/")
    public ResponseEntity<Challenge> update(@RequestBody UpdateChallengeDTO dto) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        if(isAdmin(authContext)){
            return new ResponseEntity<>(service.update(dto), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ApiOperation(value = "Delete a challenge")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        if(isAdmin(authContext)){
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
