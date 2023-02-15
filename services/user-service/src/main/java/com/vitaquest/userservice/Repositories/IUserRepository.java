package com.vitaquest.userservice.Repositories;

import com.vitaquest.userservice.Domain.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface IUserRepository extends MongoRepository<User, String> {

}

