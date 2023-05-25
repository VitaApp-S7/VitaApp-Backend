package com.vitaquest.challengeservice.Database.Repository;

import com.vitaquest.challengeservice.Domain.Models.Challenge;
import com.vitaquest.challengeservice.Domain.Models.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TeamRepository extends MongoRepository<Team, String> {
    List<Team> findByChallengeId(String challengeId);
}
