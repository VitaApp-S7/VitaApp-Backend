package com.vitaquest.moodboosterservice.Database.Repository;

import com.vitaquest.moodboosterservice.Domain.Models.UserMoodBoosterStats;
import com.vitaquest.moodboosterservice.Domain.Models.UserMoodbooster;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;
import java.util.Optional;

public interface IUserMoodboosterRepository extends MongoRepository<UserMoodbooster, String> {
    Optional<UserMoodbooster> findById(String id);
    List<UserMoodbooster> findByUserIds(String id);

    @Aggregation(pipeline = {"{'$group':{_id: {'boosterid': '$moodbooster._id','status': '$status'},'count': {'$sum': 1}}}"})
    List<UserMoodBoosterStats> getUserMoodboosterStats();
}
