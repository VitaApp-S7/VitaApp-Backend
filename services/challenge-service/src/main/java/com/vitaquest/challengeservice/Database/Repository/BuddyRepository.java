package com.vitaquest.challengeservice.Database.Repository;

import com.vitaquest.challengeservice.Domain.Models.Buddy;
import com.vitaquest.challengeservice.Domain.Models.Challenge;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BuddyRepository extends MongoRepository<Buddy, String> {
    List<Buddy> findByUserId(String userId);
}
