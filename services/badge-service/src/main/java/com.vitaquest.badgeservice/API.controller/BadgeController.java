package com.vitaquest.badgeservice.API.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.vitaquest.badgeservice.Domain.DTO.AddBadgeDTO;
import com.vitaquest.badgeservice.Domain.DTO.NewUserDTO;
import com.vitaquest.badgeservice.Domain.DTO.CompleteActivityDTO;
import com.vitaquest.badgeservice.Domain.DTO.UpdateBadgeDTO;
import com.vitaquest.badgeservice.Domain.Models.Badge;
import com.vitaquest.badgeservice.Domain.Models.UserBadge;
import com.vitaquest.badgeservice.Domain.Service.BadgeService;
import com.vitaquest.badgeservice.Utilities.AuthenticationContextValidator;
import com.vitaquest.badgeservice.Utilities.IAuthenticationValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;
import reactor.core.publisher.Mono;

import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Map;


@Slf4j
@Api(tags = "Badge Controller")
@RestController
@CrossOrigin(origins = "*")
@PreAuthorize("hasAuthority('SCOPE_User.All')")
public class BadgeController {

    private final BadgeService service;

    private final IAuthenticationValidator validator;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    @Autowired
    public BadgeController(BadgeService service, IAuthenticationValidator validator) {
        this.service = service;
        this.validator = validator;
    }


    // Send file without content header set
    // DTO known properties are: description, badgeName, requirementsComplete
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity<Badge> addBadge(@RequestPart(name = "image") MultipartFile image, @RequestPart(name = "badge") String DTO) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if (validator.isAdmin(authContext))
        {
            try
            {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                return new ResponseEntity<>(service.addBadge(image, new ObjectMapper().readValue(DTO, AddBadgeDTO.class)), httpHeaders, HttpStatus.valueOf(200));
            } catch (IOException ex)
            {
                log.error("Invalid file format");
                log.error(ex.getMessage());
                return ResponseEntity.badRequest().build();
            }
        } else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value = "Get all badges")
    @GetMapping(value = "/all")
    public @ResponseBody
    List<Badge> getAllBadges() {
        return service.getAllBadges();
    }

    @ApiOperation(value = "Get all UserBadges")
    @GetMapping(value = "/user/all")
    public @ResponseBody
    List<UserBadge> getAllUserBadges() throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.getAllUserBadges(authContext);
    }

    // Returns a badge object model along with the base64 encoded string
    @ApiOperation(value = "Get badge by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JSONObject> getBadgeById(@PathVariable("id") String id) {
        Badge badge = service.getBadgeById(id);
        if (badge != null)
        {
            return new ResponseEntity<>(badge.toJson(), HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    @ApiOperation(value = "Get userBadge by id")
    @GetMapping(value = "/{id}/user")
    public UserBadge getUserBadgeById(@PathVariable("id") String id) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        //TODO: fix image
//        if (userBadge.getBadge() != null)
//        {
//            return new ResponseEntity<>(badge.toJson(), HttpStatus.OK);
//        }
        return service.getUserBadgeById(id, authContext);
    }

    @ApiOperation(value = "Update badge")
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity<Badge> updateBadge(@RequestPart(name = "image") MultipartFile image, @RequestPart(name = "badge") String DTO) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if (validator.isAdmin(authContext))
        {
            try
            {
                return new ResponseEntity<>(service.updateBadge(image, new ObjectMapper().readValue(DTO, UpdateBadgeDTO.class)), HttpStatus.valueOf(200));
            } catch (IOException ex)
            {
                log.error("Invalid file format");
                log.error(ex.getMessage());
                return ResponseEntity.badRequest().build();
            }
        } else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value = "Delete by Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") String id) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if (validator.isAdmin(authContext))
        {
            if (service.deleteById(id))
            {
                return new ResponseEntity<>(HttpStatus.OK);
            } else return new ResponseEntity<>(HttpStatus.valueOf(404));
        } else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    @ApiOperation(value = "Test endpoint for subscriber")
    @Topic(name = "completeActivity", pubsubName = "pubsub")
    @PostMapping(value = "/completeactivity")
    public Mono<Void> activityCompleted(@RequestBody(required = false) CloudEvent<CompleteActivityDTO> cloudEvent) {
        return Mono.fromRunnable(() -> {
            try {
//                System.out.println("Subscriber got: " + cloudEvent.getData());
//                System.out.println("Subscriber got: " + OBJECT_MAPPER.writeValueAsString(cloudEvent));
//// check if cloudEventData is nul
//                CompleteActivityDTO completeActivityDTO = OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(cloudEvent.getData()), CompleteActivityDTO.class);
                CompleteActivityDTO dto = cloudEvent.getData();
                service.updateUserBadgesByActivity(dto.getActivityId(), dto.getUserIds().get(0));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @ApiOperation(value = "Test endpoint for subscriber")
    @Topic(name = "newUser", pubsubName = "pubsub")
    @PostMapping(value = "/newuser")
    public Mono<Void> newUser(@RequestBody(required = false) CloudEvent<NewUserDTO> cloudEvent) {
        return Mono.fromRunnable(() -> {
            try {
//                System.out.println("Subscriber got: " + cloudEvent.getData());
//                System.out.println("Subscriber got: " + OBJECT_MAPPER.writeValueAsString(cloudEvent));
//                NewUserDTO dto = OBJECT_MAPPER.readValue(cloudEvent.getData().toString(), NewUserDTO.class);
                service.createUserBadgesForNewUser(cloudEvent.getData().getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}

