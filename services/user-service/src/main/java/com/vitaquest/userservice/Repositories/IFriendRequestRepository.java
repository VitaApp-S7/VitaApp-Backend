package com.vitaquest.userservice.Repositories;

import com.vitaquest.userservice.Domain.DTO.FriendDTO;
import com.vitaquest.userservice.Domain.DTO.SendedFriendDTO;
import com.vitaquest.userservice.Domain.Models.FriendRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IFriendRequestRepository extends MongoRepository<FriendRequest, String> {
    List<FriendDTO> findAllByFriendId(String userId);

    List<SendedFriendDTO> findAllByUserId(String userId);
}
