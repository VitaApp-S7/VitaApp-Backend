package com.vitaquest.challengeservice.Database.Repository;


import com.vitaquest.challengeservice.Domain.Models.Buddy;
import com.vitaquest.challengeservice.Domain.Models.UserChallenge;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository extends MongoRepository<UserChallenge, String> {
//    Optional<UserChallenge> findById(String id);
    Optional<List<UserChallenge>> findUserChallengeByUserBuddysContains(List<Buddy> userBuddys);
    Optional<List<UserChallenge>> findUserChallengeByInitiatorId(String id);

}
