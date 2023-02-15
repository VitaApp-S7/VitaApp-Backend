package com.vitaquest.eventservice.Database.Repository;

import com.vitaquest.eventservice.Domain.Models.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IEventRepository extends MongoRepository<Event, String> {
}

