package com.vitaquest.userservice.Controllers;

import com.vitaquest.userservice.Domain.DTO.FriendDTO;
import com.vitaquest.userservice.Domain.DTO.SendedFriendDTO;
import com.vitaquest.userservice.Domain.Models.Friend;
import com.vitaquest.userservice.Domain.Models.FriendRequest;
import com.vitaquest.userservice.Domain.Service.FriendService;
import io.swagger.annotations.Api;
import org.apache.commons.httpclient.HttpState;
import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "User Controller")
@RestController
@CrossOrigin(origins = "*")
@PreAuthorize("hasAuthority('SCOPE_User.All')")
@RequestMapping("friends")
public class FriendController {

    private final FriendService service;

    @Autowired
    public FriendController(FriendService service) {
        this.service = service;
    }

    @PostMapping(value = "/add/{friendId}")
    public ResponseEntity<?> sentFriendRequest(@PathVariable String friendId) throws Exception {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        try {
            FriendRequest friendRequest = service.sentFriendRequest(authContext, friendId);
            return new ResponseEntity<>(friendRequest, HttpStatus.OK);
        } catch (IllegalAccessException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/requests")
    public List<FriendDTO> getFriendRequests() throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.getFriendRequests(authContext);
    }

    @GetMapping(value = "/sendedrequests")
    public List<SendedFriendDTO> getSendedFriendRequests() throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.getSendedFriendRequests(authContext);
    }

    @PostMapping(value = "/requests/accept/{friendRequestId}")
    public Friend acceptFriendRequest(@PathVariable String friendRequestId) throws Exception {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.acceptFriendRequest(authContext, friendRequestId);
    }


    @DeleteMapping(value = "/requests/cancel/{friendRequestId}")
    public Boolean cancelFriendRequest(@PathVariable String friendRequestId) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.cancelFriendRequest(authContext, friendRequestId);
    }

    @GetMapping
    public List<FriendDTO> getFriends() throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.getFriends(authContext);
    }

    @DeleteMapping(value = "/remove/{friendObjectId}")
    public void removeFriend(@PathVariable String friendObjectId) throws Exception {
        service.removeFriend(friendObjectId);
    }

}
