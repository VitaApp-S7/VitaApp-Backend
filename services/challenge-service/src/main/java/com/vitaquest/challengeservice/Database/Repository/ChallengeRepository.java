package com.vitaquest.challengeservice.Database.Repository;

import com.vitaquest.challengeservice.Domain.Models.Challenge;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface ChallengeRepository extends MongoRepository<Challenge, String>{
//    Optional<Challenge> findById(String id);
    Optional<List<Challenge>> findChallengeByStartDateBefore(Date startDate);
}
