package com.vitaquest.userservice.Domain.Service;

import com.vitaquest.userservice.Domain.DTO.SendedFriendDTO;
import com.vitaquest.userservice.Repositories.IFriendRepository;
import com.vitaquest.userservice.Repositories.IFriendRequestRepository;
import com.vitaquest.userservice.Domain.DTO.FriendDTO;
import com.vitaquest.userservice.Domain.DTO.GetFriendDTO;
import com.vitaquest.userservice.Domain.Models.Friend;
import com.vitaquest.userservice.Domain.Models.FriendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FriendService {

    private final IFriendRepository friendRepository;
    private final IFriendRequestRepository friendRequestRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public FriendService(IFriendRepository friendRepository, IFriendRequestRepository friendRequestRepository, UserService userService, NotificationService notificationService) {
        this.friendRepository = friendRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }


    public FriendRequest sentFriendRequest(Authentication authContext, String friendId) throws Exception {
        String user_id = userService.getUserInfo(authContext).getId();
        List<FriendDTO> friends = getFriends(authContext);
        List<SendedFriendDTO> sendedFriendRequestList = friendRequestRepository.findAllByUserId(userService.getUserInfo(authContext).getId());
        List<FriendDTO> friendRequests = friendRequestRepository.findAllByFriendId(userService.getUserInfo(authContext).getId());
        FriendRequest friendRequest = FriendRequest.builder()
                .userId(user_id)
                .friendId(friendId)
                .date(new Date())
                .build();


        // check if the friend request is send to the same user
        if (friendRequest.getFriendId().equals(friendRequest.getUserId())) {
            throw new IllegalAccessException("Sending a friend request to yourself is not allowed");
        }
        // check if the friend request is already sent to this person
        for (SendedFriendDTO sendedFriendDTO : sendedFriendRequestList) {
            if (sendedFriendDTO.getFriendId().equals(friendRequest.getFriendId())) {
                throw new IllegalAccessException("Friend request is already sent to this person");
            }
        }
        // check if a friend request is already received from this person
        for (FriendDTO friend : friendRequests) {
            if (friend.getUserId().equals(friendRequest.getFriendId())) {
                throw new IllegalAccessException("Friend request is already received from this person");
            }
        }
        // check if user is sending a request to an existing friend
        for (FriendDTO friendDTO : friends) {
            if (friendDTO.getUserId().equals(friendRequest.getFriendId())) {
                throw new IllegalAccessException("Sending a friend request to existing friend is not allowed");
            }
        }

        //send notification to invited user
        notificationService.HandleMessageToGivenUser(
                userService.getUserById(friendRequest.getFriendId()).getExpoToken(),
                "New friend request",
                userService.getUserById(friendRequest.getUserId()).getName() + " invited you to be a friend"
        );
        return friendRequestRepository.save(friendRequest);
    }

    public List<FriendDTO> getFriendRequests(Authentication authContext) throws IllegalAccessException {
        List<FriendDTO> friendRequests = friendRequestRepository.findAllByFriendId(userService.getUserInfo(authContext).getId());
        for (FriendDTO friend : friendRequests) {
            friend.setName(userService.getUserById(friend.getUserId()).getName());
        }
        return friendRequests;
    }

    public List<SendedFriendDTO> getSendedFriendRequests(Authentication authContext) throws IllegalAccessException {
        List<SendedFriendDTO> friendRequests = friendRequestRepository.findAllByUserId(userService.getUserInfo(authContext).getId());
        for (SendedFriendDTO friend : friendRequests) {
            friend.setName(userService.getUserById(friend.getFriendId()).getName());
        }
        return friendRequests;
    }

    public Friend acceptFriendRequest(Authentication authContext, String friendRequestId) throws Exception {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId).orElse(null);
        if (friendRequest != null && userService.getUserInfo(authContext).getId().equals(friendRequest.getFriendId())) {
            Friend friend = Friend.builder()
                    .userId(friendRequest.getUserId())
                    .friendId(friendRequest.getFriendId())
                    .build();
            friendRequestRepository.deleteById(friendRequest.getId());

            notificationService.HandleMessageToGivenUser(
                    userService.getUserById(friendRequest.getUserId()).getExpoToken(),
                    "Friend request accepted",
                    "You're now friends with " + userService.getUserById(friendRequest.getFriendId()).getName()
            );

            return friendRepository.save(friend);
        }
        throw new IllegalAccessException();
    }

    public Boolean cancelFriendRequest(Authentication authContext, String friendRequestId) throws IllegalAccessException {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId).orElse(null);
        if (friendRequest != null &&
                (userService.getUserInfo(authContext).getId().equals(friendRequest.getFriendId())
                        || userService.getUserInfo(authContext).getId().equals(friendRequest.getUserId()))
        ) {
            friendRequestRepository.deleteById(friendRequest.getId());
            return true;
        }
        throw new IllegalAccessException();
    }


    public List<FriendDTO> getFriends(Authentication authContext) throws IllegalAccessException {
        String userId = userService.getUserInfo(authContext).getId();
        List<GetFriendDTO> friends = friendRepository.findAllByUserIdOrFriendId(userId, userId);
        List<FriendDTO> friendsList = new ArrayList<>();
        for (GetFriendDTO getFriendDTO : friends) {
            FriendDTO dto;
            if (getFriendDTO.getUserId().equals(userId)) {
                dto = FriendDTO.builder()
                        .id(getFriendDTO.getId())
                        .userId(getFriendDTO.getFriendId())
                        .name(userService.getUserById(getFriendDTO.getFriendId()).getName())
                        .build();
            } else {
                dto = FriendDTO.builder()
                        .id(getFriendDTO.getId())
                        .userId(getFriendDTO.getUserId())
                        .name(userService.getUserById(getFriendDTO.getUserId()).getName())
                        .build();
            }
            friendsList.add(dto);
        }
        return friendsList;
    }

    public void removeFriend(Authentication authContext, String friendObjectId) throws Exception {
        Friend friend = friendRepository.findById(friendObjectId).get();
        String userId = userService.getUserInfo(authContext).getId();
        String userName = userService.getUserInfo(authContext).getName();
        String friendUserId = friend.getUserId();
        String friendId = friend.getFriendId();

        if(!userId.equals(friendUserId) && !userId.equals(friendId)) return;

        var notifyUserId = userId.equals(friendUserId) ? friendId : friendUserId;

        notificationService.HandleMessageToGivenUser(
                userService.getUserById(notifyUserId).getExpoToken(),
                "Friendship ended",
                "Your friendship with " + userName + " has ended"
        );

        friendRepository.deleteById(friendObjectId);
    }
}
