package com.vitaquest.userservice.Controllers;

import com.vitaquest.userservice.Domain.DTO.Notification.AllUserNotificationDTO;
import com.vitaquest.userservice.Domain.DTO.Notification.MessageToUserDTO;
import com.vitaquest.userservice.Domain.Service.NotificationService;
import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Notification Controller")
@RestController
@CrossOrigin(origins = "*")
@PreAuthorize("hasAuthority('SCOPE_User.All')")
public class NotificationController {

    private final NotificationService service;

    @Autowired
    public NotificationController (NotificationService service) {
        this.service = service;
    }

    @ApiOperation(value = "Send notification to all Users")
    @PostMapping(value = "/notifyallusers")
    @Topic(name = "notifyAllUsers", pubsubName = "pubsub")
    public void SendNotificationToAllUsers(@RequestBody(required = false) CloudEvent<AllUserNotificationDTO> cloudEvent) throws Exception {
        service.HandleAllUserNotifications(cloudEvent.getData());
    }

    @ApiOperation(value = "Test user notification")
    @PostMapping(value = "/testnotification")
    public ResponseEntity<?> testUserNotification() throws Exception {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        service.HandleUserTestNotification(authContext);
        return new ResponseEntity<>("Message send", HttpStatus.OK);
    }

    @ApiOperation(value = "Send notification to all Users")
    @PostMapping(value = "/messagetouser")
    @Topic(name = "messageFromUserToUser", pubsubName = "pubsub")
    public void SendNotificationToUser(@RequestBody(required = false) CloudEvent<MessageToUserDTO> cloudEvent) throws Exception {
        service.HandleToUserNotifications(cloudEvent.getData());
    }
}
