package com.vitaquest.challengeservice.Database.Repository;

import com.vitaquest.challengeservice.Domain.Models.Challenge;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends MongoRepository<Challenge, String>{
    List<Challenge> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(Date startDate, Date endDate);
    Optional<Challenge> findFirstByStartDateLessThanEqualAndEndDateGreaterThanEqual(Date startDate, Date endDate);
}