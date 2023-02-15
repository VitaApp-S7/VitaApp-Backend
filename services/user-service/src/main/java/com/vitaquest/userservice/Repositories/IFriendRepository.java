package com.vitaquest.userservice.Repositories;

import com.vitaquest.userservice.Domain.DTO.GetFriendDTO;
import com.vitaquest.userservice.Domain.Models.Friend;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IFriendRepository extends MongoRepository<Friend, String> {
    List<GetFriendDTO> findAllByUserIdOrFriendId(String id, String id1);
}
