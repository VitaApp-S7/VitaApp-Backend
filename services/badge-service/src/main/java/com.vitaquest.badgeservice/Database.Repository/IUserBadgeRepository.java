package com.vitaquest.badgeservice.Database.Repository;

import com.vitaquest.badgeservice.Domain.Models.Badge;
import com.vitaquest.badgeservice.Domain.Models.UserBadge;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IUserBadgeRepository extends MongoRepository<UserBadge, String> {
    List<UserBadge> findByUserId(String id);
    List<UserBadge> findByUserIdIn(List<String> ids);
}
