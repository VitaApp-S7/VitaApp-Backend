package com.vitaquest.badgeservice.Database.Repository;

import com.vitaquest.badgeservice.Domain.Models.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IBadgeRepository extends MongoRepository<Badge, String> {
}
