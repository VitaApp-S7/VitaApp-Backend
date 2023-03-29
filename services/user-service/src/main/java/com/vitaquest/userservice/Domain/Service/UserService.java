package com.vitaquest.userservice.Domain.Service;

import com.vitaquest.userservice.Domain.DTO.PublicUserDTO;
import com.vitaquest.userservice.Repositories.IUserListRepository;
import com.vitaquest.userservice.Repositories.IUserRepository;
import com.vitaquest.userservice.Domain.DTO.CompleteMoodboosterDTO;
import com.vitaquest.userservice.Domain.DTO.NewUserDTO;
import com.vitaquest.userservice.Domain.Models.User;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final IUserRepository repository;
    private final IUserListRepository listRepository;
    private final DaprClient daprClient;

    @Autowired
    public UserService(IUserRepository repository, IUserListRepository listRepository) {
        this.repository = repository;
        this.listRepository = listRepository;
        this.daprClient = new DaprClientBuilder().build();
    }

    // public User addUser(AddUserDTO dto) {
    // User user = User.builder()
    // .id(dto.getId())
    // .name(dto.getName())
    // .email(dto.getEmail())
    // .mood(10)
    // .build();
    // return repository.save(user);
    // }

    public void increaseUserMood(CompleteMoodboosterDTO dto) throws IllegalAccessException {
        for (String userid: dto.getUserIds()) {
            User user = getUserById(userid);
            if(user.getMood() + dto.getPoints() > 20){
                user.setMood(20);
            }
            else {
                user.setMood(user.getMood() + dto.getPoints());
            }

            repository.save(user);
        }
    }

    public User decreaseUserMood(Authentication authContext, Integer decreaseAmount) throws IllegalAccessException {
        User user = getUserInfo(authContext);
        user.setMood(user.getMood() - decreaseAmount);
        return repository.save(user);
    }

    public User setUserMood(Authentication authContext, Integer mood) throws IllegalAccessException {
        User user = getUserInfo(authContext);
        user.setMood(mood);
        return repository.save(user);
    }

    public boolean isFirstLogin(Authentication authContext) throws IllegalAccessException {
        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authContext.getPrincipal(), "claims", true);
        boolean exists = repository.existsById((String) claims.get("oid"));
        if (exists) {
            return false;
        } else {
            User user = new User((String) claims.get("oid"), (String) claims.get("name"),
                    (String) claims.get("preferred_username"), 10, 0);
            user = repository.save(user);
            NewUserDTO dto = new NewUserDTO();
            dto.setId(user.getId());

            // Todo: wrap blocking call
            daprClient.publishEvent("pubsub", "newUser", dto).block();

        }
        return true;
    }

    /*
     * This function should only be called after isFirstLogin() to ensure that there
     * is always data available.
     */
    public User getUserInfo(Authentication authContext) throws IllegalAccessException {
        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authContext.getPrincipal(), "claims", true);
        return repository.findById((String) claims.get("oid")).get();
    }

    public Page<PublicUserDTO> getAllUsersByPageApp(Integer pageNumber, Integer pageSize) {
        Page<User> sortUsers = listRepository.findAll(PageRequest.of(pageNumber, pageSize));
        List<User> users = sortUsers.getContent();
        
        return sortUsers.map(user -> {
            PublicUserDTO publicUser = new PublicUserDTO();
            publicUser.setId(user.getId());
            publicUser.setName(user.getName());
            return publicUser;
        });
    }

    public List<PublicUserDTO> getAllUsersByPage(Integer pageNumber) {
        List<PublicUserDTO> publicUsers = new ArrayList<>();
        // sort users per page
        Page<User> sortUsers = listRepository.findAll(PageRequest.of(pageNumber, 10));
        List<User> users = sortUsers.getContent();
        // display name and id of public users
        for (User user : users) {
            PublicUserDTO publicUser = new PublicUserDTO();
            publicUser.setId(user.getId());
            publicUser.setName(user.getName());
            publicUsers.add(publicUser);
        }
        return publicUsers;
    }

    public List<PublicUserDTO> getAllPublicUsers() {
        List<PublicUserDTO> publicUsers = new ArrayList<>();
        List<User> users = repository.findAll();
        for (User user : users) {
            PublicUserDTO publicUser = new PublicUserDTO();
            publicUser.setId(user.getId());
            publicUser.setName(user.getName());
            publicUsers.add(publicUser);
        }
        return publicUsers;
    }

    public List<User> getAllUsers() {
        List<User> users = repository.findAll();
        return users;
    }

    public User getUserById(String userId) {
        return repository.findById(userId).orElse(null);
    }

    // public boolean deleteUser(long userid){
    //
    // }

    public void increaseUserPointsEarned(CompleteMoodboosterDTO dto) throws IllegalAccessException {
        //Double amount of points if moodbooster is performed together
        int points = dto.getPoints();
        if(dto.getUserIds().size() > 1)
            points *= 2;

        for (String userid: dto.getUserIds()) {
            User user = getUserById(userid);
            if (user.getPointsEarned() != null) {
                user.setPointsEarned(user.getPointsEarned() + points);
            } else {
                user.setPointsEarned(points);
            }
            repository.save(user);
        }
    }

    public User setExpoToken(Authentication authContext, String token) throws IllegalAccessException {
        User user = getUserInfo(authContext);
        user.setExpoToken(token);
        return repository.save(user);
    }

    public String getExpoToken(Authentication authContext) throws IllegalAccessException {
        User user = getUserInfo(authContext);
        return user.getExpoToken();
    }

    public User setModalVisible(Authentication authContext, boolean isModalVisible) throws IllegalAccessException {
        User user = getUserInfo(authContext);
        user.setModalVisable(isModalVisible);
        return repository.save(user);
    }

    public boolean getModalVisible(Authentication authContext) throws IllegalAccessException {
        User user = getUserInfo(authContext);
        return user.getModalVisable();
    }

    public User setDate(Authentication authContext, String date) throws IllegalAccessException {
        User user = getUserInfo(authContext);
        user.setDate(date);
        return repository.save(user);
    }

    public String getDate(Authentication authContext) throws IllegalAccessException {
        User user = getUserInfo(authContext);
        return user.getDate();
    }
}
