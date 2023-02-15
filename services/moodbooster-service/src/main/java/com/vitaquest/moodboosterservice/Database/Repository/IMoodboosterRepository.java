package com.vitaquest.moodboosterservice.Database.Repository;

import com.vitaquest.moodboosterservice.Domain.Models.Moodbooster;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IMoodboosterRepository extends MongoRepository<Moodbooster, String> {
    Optional<Moodbooster> findById(String id);
}
