package com.vitaquest.moodboosterservice.Database.Repository;

import com.vitaquest.moodboosterservice.Domain.Models.UserMoodboosterInvite;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IUserMoodboosterInviteRepository extends MongoRepository<UserMoodboosterInvite, String> {
    List<UserMoodboosterInvite> getAllByInvitedUserId(String invitedUserId);
    void deleteAllByUserMoodboosterId(String userMoodboosterId);
    List<UserMoodboosterInvite> getAllByUserMoodboosterId(String userMoodboosterId);
    List<UserMoodboosterInvite> getAllDistinctByUserMoodboosterId(String userMoodboosterId);
    Optional<UserMoodboosterInvite> findByInvitedUserIdAndUserMoodboosterId(String InvitedUserId, String UserMoodboosterId);
}
