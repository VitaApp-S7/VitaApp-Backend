package com.vitaquest.moodboosterservice.Domain.Service;

import com.vitaquest.moodboosterservice.Database.Repository.IMoodboosterRepository;
import com.vitaquest.moodboosterservice.Database.Repository.IUserMoodboosterInviteRepository;
import com.vitaquest.moodboosterservice.Database.Repository.IUserMoodboosterRepository;
import com.vitaquest.moodboosterservice.Domain.DTO.AddMoodboosterDTO;
import com.vitaquest.moodboosterservice.Domain.DTO.CompleteMoodboosterDTO;
import com.vitaquest.moodboosterservice.Domain.DTO.Notification.MessageToUserDTO;
import com.vitaquest.moodboosterservice.Domain.DTO.UpdateMoodboosterDTO;
import com.vitaquest.moodboosterservice.Domain.Models.*;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

import java.util.*;

@Service
public class MoodboosterService {
    private final List<Moodbooster> Moodboosters = new ArrayList<>();
    private final IMoodboosterRepository repository;
    private final IUserMoodboosterRepository userMoodboostersRepository;
    private final IUserMoodboosterInviteRepository userMoodboosterInviteRepository;

    private final DaprClient daprClient;

    @Autowired
    public MoodboosterService(IMoodboosterRepository repository, IUserMoodboosterRepository userMoodboostersRepository, IUserMoodboosterInviteRepository userMoodboosterInviteRepository) {
        this.repository = repository;
        this.daprClient = new DaprClientBuilder().build();
        this.userMoodboostersRepository = userMoodboostersRepository;
        this.userMoodboosterInviteRepository = userMoodboosterInviteRepository;
    }

    public Moodbooster addMoodbooster(AddMoodboosterDTO DTO)
    {
        Moodbooster moodbooster = Moodbooster.builder()
                .title(DTO.getTitle())
                .status(DTO.getStatus())
                .description(DTO.getDescription())
                .points(DTO.getPoints())
                .category(DTO.getCategory())
                .build();
        moodbooster = repository.save(moodbooster);
        return moodbooster;
    }

    public Moodbooster updateMoodbooster(UpdateMoodboosterDTO DTO){
        // get existing booster by id
        Moodbooster existingMoodbooster = getMoodboosterByID(DTO.getId());
        // update booster fields
        existingMoodbooster.setTitle(DTO.getTitle());
        existingMoodbooster.setDescription(DTO.getDescription());
        existingMoodbooster.setStatus(DTO.getStatus());
        existingMoodbooster.setPoints(DTO.getPoints());
        existingMoodbooster.setCategory(DTO.getCategory());
        // update booster in db
        repository.save(existingMoodbooster);
        return existingMoodbooster;
    }

    public Moodbooster getMoodboosterByID(String moodboosterId)
    {
        Optional<Moodbooster> foundMoodbooster = repository.findById(moodboosterId);
        if(foundMoodbooster.isEmpty())
        {
           //exception
        }
        return foundMoodbooster.get();
    }

    public List<Moodbooster> getAllMoodboosters() {
        return repository.findAll();
    }

    public void deleteMoodboosterByID(String moodboosterID) {
        Optional<Moodbooster> foundMoodbooster = repository.findById(moodboosterID);
        if (foundMoodbooster.isEmpty())
        {
            //throw not found exception
        }
        Moodbooster moodbooster = foundMoodbooster.get();
        Moodboosters.remove(moodbooster);
        repository.delete(moodbooster);
    }

    public UserMoodbooster acceptMoodbooster(String moodboosterID, Authentication authContext) throws IllegalAccessException {
        Moodbooster moodbooster = getMoodboosterByID(moodboosterID);
        String userId = getUserIdFromAuthContext(authContext);

        UserMoodbooster userMoodbooster = new UserMoodbooster();
        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        userMoodbooster.setMoodbooster(moodbooster);
        userMoodbooster.setStatus(UserMoodboosterStatus.ACCEPTED);
        userMoodbooster.setUserIds(userIds);

        return userMoodboostersRepository.save(userMoodbooster);
    }

    public UserMoodbooster completeMoodbooster(Authentication authContext, String userMoodboosterID) throws IllegalAccessException {
        UserMoodbooster userMoodbooster = userMoodboostersRepository.findById(userMoodboosterID).orElse(null);

        if(!userMoodbooster.getUserIds().contains(getUserIdFromAuthContext(authContext)))
            throw new IllegalAccessException();

        userMoodbooster.setStatus(UserMoodboosterStatus.COMPLETE);
        userMoodbooster.setCompletionDate(new Date());

        CompleteMoodboosterDTO dto = CompleteMoodboosterDTO.builder()
                .points(userMoodbooster.getMoodbooster().getPoints())
                .userIds(userMoodbooster.getUserIds())
                .build();

        daprClient.publishEvent("pubsub", "completeMoodbooster", dto).block();
        userMoodboosterInviteRepository.deleteAllByUserMoodboosterId(userMoodboosterID); //Delete all related invites
        return userMoodboostersRepository.save(userMoodbooster);
    }

    public boolean cancelMoodbooster(String userMoodboosterID)
    {
        Optional<UserMoodbooster> userMoodbooster = userMoodboostersRepository.findById(userMoodboosterID);

        userMoodbooster.get().getUserIds();

        if(userMoodbooster.isPresent()){
            UserMoodbooster booster = userMoodbooster.get();
            booster.setStatus(UserMoodboosterStatus.CANCELED);
            userMoodboostersRepository.save(booster);
            userMoodboosterInviteRepository.deleteAllByUserMoodboosterId(userMoodboosterID); //Delete all related invites
            return true;
        }
        return false;
    }

    public List<UserMoodbooster> getAllCompletedMoodboosters(Authentication authContext) throws IllegalAccessException {
        List<UserMoodbooster> completedMoodboosters = new ArrayList<>();
        String userId = getUserIdFromAuthContext(authContext);
        for (UserMoodbooster userMoodbooster : userMoodboostersRepository.findByUserIds(userId))
        {
            if (userMoodbooster.getStatus() == UserMoodboosterStatus.COMPLETE)
            {
                completedMoodboosters.add(userMoodbooster);
            }
            //exception
        }
        return completedMoodboosters;
    }

    public List<UserMoodbooster> getAllAcceptedMoodboosters(Authentication authContext) throws IllegalAccessException {
        List<UserMoodbooster> acceptedMoodboosters = new ArrayList<>();
        String userId = getUserIdFromAuthContext(authContext);
        for (UserMoodbooster moodbooster : userMoodboostersRepository.findByUserIds(userId))
        {

            if (moodbooster.getStatus() == UserMoodboosterStatus.ACCEPTED)
            {
                acceptedMoodboosters.add(moodbooster);
            }
            //exception
        }
        return acceptedMoodboosters;
    }

    public List<Moodbooster> getAllActiveMoodboosters() {
        List<Moodbooster> activeMoodboosters = new ArrayList<>();
        for (Moodbooster moodbooster : repository.findAll())
        {
            if (moodbooster.getStatus() == Status.ACTIVE)
            {
                activeMoodboosters.add(moodbooster);
            }
            //exception
        }
        return activeMoodboosters;
    }

    public UserMoodboosterInvite inviteUser(String userMoodboosterId, String invitedUserId, Authentication authContext) throws IllegalAccessException {
        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authContext.getPrincipal(), "claims", true);
        String username = claims.get("name").toString();
        String userId = getUserIdFromAuthContext(authContext);
        Optional<UserMoodbooster> userMoodbooster = userMoodboostersRepository.findById(userMoodboosterId);

        if(userMoodbooster.isEmpty())
            throw new IllegalAccessException("Invalid moodbooster");

        if(userMoodboosterInviteRepository.findByInvitedUserIdAndUserMoodboosterId(invitedUserId, userMoodboosterId).isPresent())
            throw new IllegalAccessException("This user has already been invited");

        UserMoodboosterInvite userMoodboosterInvite = UserMoodboosterInvite.builder()
                .userMoodboosterId(userMoodboosterId)
                .inviterName(username)
                .inviterId(userId)
                .invitedUserId(invitedUserId)
                .moodboosterName(userMoodbooster.get().getMoodbooster().getTitle())
                .moodboosterDescription(userMoodbooster.get().getMoodbooster().getDescription())
                .build();

        MessageToUserDTO dto = MessageToUserDTO.builder()
                .receiver(invitedUserId)
                .title("New Moodbooster Invite")
                .message(username + " invited you to perform a moodbooster together!")
                .build();
        daprClient.publishEvent("pubsub", "messageToUser", dto).block();
        return userMoodboosterInviteRepository.save(userMoodboosterInvite);
    }

    public List<UserMoodboosterInvite> getInvites(Authentication authContext ) throws IllegalAccessException {
        String userId = getUserIdFromAuthContext(authContext);
        return userMoodboosterInviteRepository.getAllByInvitedUserId(userId);
    }

    public UserMoodbooster acceptMoodboosterInvite(Authentication authContext, String inviteId) throws IllegalAccessException {
        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authContext.getPrincipal(), "claims", true);
        String username = claims.get("name").toString();
        String userId = getUserIdFromAuthContext(authContext);
        UserMoodboosterInvite invite = userMoodboosterInviteRepository.findById(inviteId).orElse(null);

        if(invite == null)
            throw new IllegalAccessException("Invite not found");

        if(!invite.getInvitedUserId().equals(userId))
            throw new IllegalAccessException("You can not perform this action");

        UserMoodbooster userMoodbooster = userMoodboostersRepository.findById(invite.getUserMoodboosterId()).orElse(null);

        if(userMoodbooster == null)
            throw new IllegalAccessException("Moodbooster not found");

        List<String> userIds = userMoodbooster.getUserIds();
        userIds.add(invite.getInvitedUserId());
        userMoodbooster.setUserIds(userIds);

        MessageToUserDTO dto = MessageToUserDTO.builder()
                .receiver(invite.getInviterId())
                .title("Moodbooster Invite Accepted")
                .message(username + " accepted your moodbooster invite")
                .build();
        daprClient.publishEvent("pubsub", "messageToUser", dto).block();

        userMoodboosterInviteRepository.deleteById(inviteId);
        return userMoodboostersRepository.save(userMoodbooster);
    }

    public boolean declineMoodboosterInvite(Authentication authContext, String inviteId) throws IllegalAccessException {
        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authContext.getPrincipal(), "claims", true);
        String username = claims.get("name").toString();
        String userId = getUserIdFromAuthContext(authContext);
        UserMoodboosterInvite invite = userMoodboosterInviteRepository.findById(inviteId).orElse(null);

        if(invite == null || !invite.getInvitedUserId().equals(userId))
            return false;

        MessageToUserDTO dto = MessageToUserDTO.builder()
                .receiver(invite.getInviterId())
                .title("Moodbooster Invite Declined")
                .message(username + " declined your moodbooster invite")
                .build();
        daprClient.publishEvent("pubsub", "messageToUser", dto).block();

        userMoodboosterInviteRepository.deleteById(inviteId);
        return true;
    }

    private String getUserIdFromAuthContext(Authentication authContext) throws IllegalAccessException {
        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authContext.getPrincipal(), "claims", true);
        return claims.get("oid").toString();
    }
}
