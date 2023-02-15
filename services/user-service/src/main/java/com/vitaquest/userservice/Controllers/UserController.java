package com.vitaquest.userservice.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.vitaquest.userservice.Domain.DTO.CompleteMoodboosterDTO;
import com.vitaquest.userservice.Domain.DTO.PublicUserDTO;
import com.vitaquest.userservice.Domain.Models.User;
import com.vitaquest.userservice.Domain.Service.UserService;
import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = "User Controller")
@RestController
@CrossOrigin(origins = "*")
@PreAuthorize("hasAuthority('SCOPE_User.All')")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService service;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

//    @ApiOperation(value = "Add user")
//    @PostMapping
//    public @ResponseBody User addUser(@RequestBody AddUserDTO DTO) {
//        return service.addUser(DTO);
//    }

    @ApiOperation(value = "Complete mood booster")
    @PostMapping(value = "/completemoodbooster")
    @Topic(name = "completeMoodbooster", pubsubName = "pubsub")
    public void increaseMood(@RequestBody(required = false) CloudEvent<CompleteMoodboosterDTO> cloudEvent) throws IllegalAccessException {
        service.increaseUserPointsEarned(cloudEvent.getData());
        service.increaseUserMood(cloudEvent.getData());
    }
    @ApiOperation(value = "Decrease user mood")
    @PostMapping(value = "/decreasemood/{decreaseAmount}")
    public User decreaseMood(@PathVariable Integer decreaseAmount) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.decreaseUserMood(authContext, decreaseAmount);
    }
    @ApiOperation(value = "Set user mood")
    @PostMapping(value = "/setmood/{amount}")
    public User setMood(@PathVariable Integer amount) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.setUserMood(authContext, amount);
    }

    @ApiOperation(value = "Get all users as an admin")
    @GetMapping(value = "/all")
    public List<User> getAllUsers() throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        if(isAdmin(authContext)){
            return service.getAllUsers();
        }
        throw new IllegalAccessException();
    }

    @ApiOperation(value = "Get public users per page")
    @GetMapping(value = "/public/page/{pageNumber}")
    public List<PublicUserDTO> getPublicUsers(@PathVariable Integer pageNumber) {
        return service.getAllUsersByPage(pageNumber);
    }

    @ApiOperation(value = "Get all public users")
    @GetMapping(value = "/public/all")
    public List<PublicUserDTO> getAllPublicUsers() {
        return service.getAllPublicUsers();
    }


    @ApiOperation(value = "Get users by ID")
    @GetMapping(value = "/{userId}")
    public @ResponseBody User getUserById(@PathVariable String userId) {
       User user = service.getUserById(userId);
       if (user == null) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND);
       } else {
            return user;
       }
    }



    @ApiOperation(value = "On login, checks if this is the user's first time. If it is, the user data is parsed from the token claims and saved to the database")
    @GetMapping("/login/check")
    public boolean isFirstLogin() throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.isFirstLogin(authContext);
    }

    @ApiOperation(value = "Returns user info of the authenticated user")
    @GetMapping("/me")
    public User getUserInfo() throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.getUserInfo(authContext);
    }

    @ApiOperation(value = "Set user ExpoToken")
    @PostMapping(value = "/setexpo/{token}")
    public User setExpoToken(@PathVariable String token) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.setExpoToken(authContext, token);
    }

    @ApiOperation(value = "Get user ExpoToken")
    @GetMapping(value = "/expo")
    public String getExpoToken() throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.getExpoToken(authContext);
    }

    @ApiOperation(value = "Set user modalvisible")
    @PostMapping(value = "/modalvisible/{isModalVisible}")
    public User setModalVisible(@PathVariable boolean isModalVisible) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.setModalVisible(authContext, isModalVisible);
    }

    @ApiOperation(value = "Get user modalvisible")
    @GetMapping(value = "/modalvisible")
    public Boolean getModalVisible() throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.getModalVisible(authContext);
    }

    @ApiOperation(value = "Set user date")
    @PostMapping(value = "/date/{date}")
    public User setDate(@PathVariable String date) throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.setDate(authContext, date);
    }

    @ApiOperation(value = "Get user date")
    @GetMapping(value = "/date")
    public String getDate() throws IllegalAccessException {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        return service.getDate(authContext);
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
