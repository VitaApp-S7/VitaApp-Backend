package com.vitaquest.moodboosterservice.Database.Repository;

import com.vitaquest.moodboosterservice.Domain.Models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ICategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findById(String id);

}
