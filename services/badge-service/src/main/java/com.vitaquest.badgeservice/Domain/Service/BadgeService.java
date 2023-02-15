package com.vitaquest.badgeservice.Domain.Service;

import com.vitaquest.badgeservice.Database.Repository.IBadgeRepository;
import com.vitaquest.badgeservice.Database.Repository.IUserBadgeRepository;
import com.vitaquest.badgeservice.Domain.DTO.AddBadgeDTO;
import com.vitaquest.badgeservice.Domain.DTO.UpdateBadgeDTO;
import com.vitaquest.badgeservice.Domain.Models.Badge;
import com.vitaquest.badgeservice.Domain.Models.UserBadge;
import com.vitaquest.badgeservice.Domain.Models.UserBadgeStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class BadgeService {

    private final IBadgeRepository repository;
    private final IUserBadgeRepository userBadgeRepository;

    public BadgeService(IBadgeRepository repository, IUserBadgeRepository userBadgeRepository){
        this.repository = repository;
        this.userBadgeRepository = userBadgeRepository;
    }

    public Badge addBadge(MultipartFile image, AddBadgeDTO dto) throws IOException {

        Badge badge = Badge.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .activityId(dto.getActivityId())
                .bronzeTreshold(dto.getBronzeTreshold())
                .silverTreshold(dto.getSilverTreshold())
                .goldTreshold(dto.getGoldTreshold())
                .status(dto.getStatus())
                .imageSet(new Binary(BsonBinarySubType.BINARY, image.getBytes()))
                .build();
        createUserBadgesForNewBadge(badge);
        return repository.save(badge);
    }

    public List<Badge> getAllBadges() {
        return repository.findAll();
    }

    public List<UserBadge> getAllUserBadges(Authentication authContext) throws IllegalAccessException {
        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authContext.getPrincipal(), "claims", true);
        String userId = claims.get("oid").toString();
        return userBadgeRepository.findByUserId(userId);
    }

    public Badge getBadgeById(String badgeID) {
        return repository.findById(badgeID).orElse(null);
    }

    public UserBadge getUserBadgeById(String badgeID, Authentication authContext) throws IllegalAccessException {
        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authContext.getPrincipal(), "claims", true);
        String userId = claims.get("oid").toString();
        List<UserBadge> userBadges = userBadgeRepository.findByUserId(userId);
        for (UserBadge userBadge :
                userBadges) {
            if (userBadge.getBadge().getId().equals(badgeID)) {
                return userBadge;
            }
        }
        throw new NoSuchElementException();
    }

    public Badge updateBadge(MultipartFile image, UpdateBadgeDTO dto) throws IOException {
        Badge badge = Badge.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .activityId(dto.getActivityId())
                .bronzeTreshold(dto.getBronzeTreshold())
                .silverTreshold(dto.getSilverTreshold())
                .goldTreshold(dto.getGoldTreshold())
                .status(dto.getStatus())
                .imageSet(new Binary(BsonBinarySubType.BINARY, image.getBytes()))
                .build();
        return repository.save(badge);
    }


    public boolean deleteById(String id) {
        if(repository.existsById(id)){
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public void createUserBadgesForNewBadge(Badge badge){
        //TODO: get all userIds
        List<String> users = new ArrayList<>();
        List<UserBadge> userBadges = new ArrayList<>();

        for (String userId :
                users) {
            UserBadge userBadge = new UserBadge();
            userBadge.setBadge(badge);
            userBadge.setUserId(userId);
            userBadge.setStatus(UserBadgeStatus.GREY);
            userBadge.setCount(0);
            userBadges.add(userBadge);
        }
        userBadgeRepository.saveAll(userBadges);
    }

    //TODO: get message of new user
    public void createUserBadgesForNewUser(String userId){
        List<Badge> badges = repository.findAll();
        List<UserBadge> userBadges = new ArrayList<>();

        for (Badge badge :
                badges) {
            UserBadge userBadge = new UserBadge();
            userBadge.setBadge(badge);
            userBadge.setUserId(userId);
            userBadge.setStatus(UserBadgeStatus.GREY);
            userBadge.setCount(0);
            userBadges.add(userBadge);
        }
        userBadgeRepository.saveAll(userBadges);
    }

    public void updateUserBadgesByActivity(String activityId, String userId) {
        List<UserBadge> userBadges = userBadgeRepository.findByUserId(userId);
        for (UserBadge userBadge : userBadges
        ) {
            if (userBadge.getBadge().getActivityId().equals(activityId)) {
                userBadge.setCount(userBadge.getCount() + 1);
                if (userBadge.getCount() >= userBadge.getBadge().getGoldTreshold()) {
                    userBadge.setStatus(UserBadgeStatus.GOLD);
                } else if (userBadge.getCount() >= userBadge.getBadge().getSilverTreshold()) {
                    userBadge.setStatus(UserBadgeStatus.SILVER);
                } else if (userBadge.getCount() >= userBadge.getBadge().getBronzeTreshold()) {
                    userBadge.setStatus(UserBadgeStatus.BRONZE);
                } else {
                    userBadge.setStatus(UserBadgeStatus.GREY);
                }
            }
        }
        System.out.println(userBadges.toString());
        userBadgeRepository.saveAll(userBadges);
    }
}
