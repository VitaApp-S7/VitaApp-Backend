package com.vitaquest.eventservice.API.Controller;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.vitaquest.eventservice.Domain.DTO.AddEventDTO;
import com.vitaquest.eventservice.Domain.DTO.UpdateEventDTO;
import com.vitaquest.eventservice.Domain.Models.Event;
import com.vitaquest.eventservice.Domain.Service.EventService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "Event Controller")
@RestController
@CrossOrigin(origins = "*")
@PreAuthorize("hasAuthority('SCOPE_User.All')")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @ApiOperation(value = "Get all events")
    @GetMapping(value = "/all")
    public List<Event> getAllUsers() {
        return service.getAllEvents();
    }

    @ApiOperation(value = "Add event")
    @PostMapping(value = "/add")
    public @ResponseBody ResponseEntity<Event> addEvent(@RequestBody AddEventDTO DTO) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        if(isAdmin(authContext)){
            return new ResponseEntity<>(service.addEvent(DTO), HttpStatus.CREATED);
        }
        throw new IllegalAccessException();
    }

    @ApiOperation(value = "Update event")
    @PutMapping("/update")
    public @ResponseBody ResponseEntity<Event> updateEvent(@RequestBody UpdateEventDTO DTO) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authContext)) {
            return new ResponseEntity<>(service.updateEvent(DTO), HttpStatus.CREATED);
        }
        else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value = "Delete event")
    @DeleteMapping(value = "/{eventId}")
    public HttpStatus delete(@PathVariable String eventId) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if(isAdmin(authContext)) {
            if(service.deleteEvent(eventId, authContext)){
                return HttpStatus.valueOf(200);
            }
            return HttpStatus.BAD_REQUEST;
        }

        throw new IllegalAccessException();
    }

    //add user to event
    @ApiOperation(value = "Add user to Event")
    @PutMapping(value = "/join/{eventId}")
    public Event addUserToEvent(@PathVariable String eventId) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.acceptEvent(eventId, authContext);
    }

    //remove user from event
    @ApiOperation(value = "remove user to Event")
    @PutMapping(value = "/leave/{eventId}")
    public Event removeUserFromEvent(@PathVariable String eventId) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.removeFromEvent(eventId, authContext);
    }

    @ApiOperation(value = "Add or remove user from event")
    @PutMapping(value = "/check-user/{eventId}")
    public Event CheckIfUserIsInEvent(@PathVariable String eventId) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.CheckIfUserIsInEvent(eventId, authContext);
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
