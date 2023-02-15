package com.vitaquest.feedservice.Database.Repository;

import com.vitaquest.feedservice.Domain.Models.News;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface INewsRepository extends MongoRepository<News, String> {

}
