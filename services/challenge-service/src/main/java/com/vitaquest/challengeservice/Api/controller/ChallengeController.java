package com.vitaquest.challengeservice.Api.controller;

import com.google.common.collect.Lists;
import com.vitaquest.challengeservice.Domain.DTO.AddChallengeDTO;
import com.vitaquest.challengeservice.Domain.DTO.UpdateChallengeDTO;
import com.vitaquest.challengeservice.Domain.Models.Challenge;
import com.vitaquest.challengeservice.Domain.Models.UserChallenge;
import com.vitaquest.challengeservice.Domain.Service.ChallengeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Api(tags = "Challenge Controller")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAuthority('SCOPE_User.All')")
@RestController
public class ChallengeController {

    private final ChallengeService service;

    public ChallengeController(ChallengeService service){
        this.service = service;
    }

    // Todo: com.vitaquest.challengeservice.Authentication checks, error handling, pageables

    // Todo: error handling, pageables
//    @ApiOperation(value = "Get all challenges by category")
//    @GetMapping("/category/{categoryName}")
//    public ResponseEntity<List<Challenge>> getChallengeByCategory(@PathVariable String categoryName){
//        return new ResponseEntity<>(service.getChallengeByCategory(categoryName).get(), HttpStatus.valueOf(200));
//    }

    // Todo: make this pageable
    @ApiOperation(value = "Get all challenges in pages")
    @GetMapping("/all/pages")
    public ResponseEntity<List<Challenge>> getAllPageable(@RequestParam("pages") int pages){
        Pageable pageable = PageRequest.of(pages, 5);
        Page<Challenge> page = service.findAll(pageable);
        //Optional<List<Challenge>> challenges = service.getAll();
        return new ResponseEntity<>(Lists.newArrayList(page), HttpStatus.valueOf(200));
    }

    @ApiOperation(value = "Get all challenges")
    @GetMapping("/all")
    public ResponseEntity<List<Challenge>> getAll(){
        Optional<List<Challenge>> challenges = service.getAll();
        return challenges.map(challengeList -> new ResponseEntity<>(challengeList, HttpStatus.valueOf(200))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Get all available challenges")
    @GetMapping("/available")
    public ResponseEntity<List<Challenge>> getAvailable(){
        Optional<List<Challenge>> challenges = service.getAvailable();
        return challenges.map(challengeList -> new ResponseEntity<>(challengeList, HttpStatus.valueOf(200))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Get challenge by id")
    @GetMapping("/{id}")
    public ResponseEntity<Challenge> getById(@PathVariable String id){
        Optional<Challenge> challenge = service.findById(id);
        return challenge.map(value -> new ResponseEntity<>(value, HttpStatus.valueOf(200))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Add a challenge")
    @PostMapping("/")
    public ResponseEntity<Object> add(@RequestBody AddChallengeDTO dto){
        try{
            Challenge challenge = service.createChallenge(dto);
            if(challenge != null){
                return new ResponseEntity<>(challenge, HttpStatus.CREATED);
            }
            return new ResponseEntity<>("Challenge creation failed", HttpStatus.BAD_REQUEST);
        }
        catch (ParseException e){
            return new ResponseEntity<>("Invalid date format", HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Update a challenge")
    @PutMapping("/")
    public ResponseEntity<Object> update(@RequestBody UpdateChallengeDTO dto){
        try{
            Challenge challenge = service.updateChallenge(dto);
            if(challenge != null){
                return new ResponseEntity<>(challenge, HttpStatus.CREATED);
            };
            return new ResponseEntity<>("No challenge found", HttpStatus.NOT_FOUND);
        }
        catch (ParseException e){
            return new ResponseEntity<>("Invalid date format", HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Delete a challenge")
    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable String id){
        if(service.deleteChallenge(id)){
            return HttpStatus.valueOf(200);
        }
        return HttpStatus.BAD_REQUEST;
    }

    // Todo: Error handling
    @ApiOperation(value = "Accept a challenge")
    @PutMapping("/accept/{userChallengeId}")
    public ResponseEntity<Object> acceptChallenge(@PathVariable String userChallengeId){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        try{
            return new ResponseEntity<>(service.acceptChallenge(userChallengeId, authContext), HttpStatus.valueOf(200));
        }
        catch(IllegalAccessException e){
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "invite a user to a challenge")
    @PutMapping("/{userChallengeId}/invite/{userId}")
    public ResponseEntity<Object> inviteUser(@PathVariable String userChallengeId, @PathVariable String userId){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        try{
            return new ResponseEntity<>(service.inviteUser(userChallengeId, userId, authContext), HttpStatus.valueOf(200));
        }
        catch(IllegalAccessException e){
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Open a challenge")
    @PostMapping("/open/{challengeId}")
    public ResponseEntity<UserChallenge> openChallenge(@PathVariable String challengeId){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        try{
            return new ResponseEntity<>(service.openChallenge(challengeId, authContext), HttpStatus.valueOf(200));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation(value = "Start a challenge")
    @PutMapping("/start/{userChallengeId}")
    public ResponseEntity<UserChallenge> startChallenge(@PathVariable String userChallengeId){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        try{
            return new ResponseEntity<>(service.startChallenge(userChallengeId, authContext), HttpStatus.valueOf(200));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation(value = "Cancel a challenge")
    @PutMapping("/cancel/{id}")
    public ResponseEntity<Object> cancelChallenge(@PathVariable String id){
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        try
        {
            return new ResponseEntity<>(service.cancelChallenge(id,authContext),HttpStatus.valueOf(200));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().build();
        }
    }

    // Todo: error handling;
    @ApiOperation(value = "Get active challenges by user")
    @GetMapping("/active")
    public ResponseEntity<List<UserChallenge>> getActiveByUser() throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(service.getActiveByUserId(authContext).get(), HttpStatus.valueOf(200));
    }

}
