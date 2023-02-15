package com.vitaquest.userservice.Domain.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetFriendDTO {
    private String id;
    private String userId;
    private String friendId;
}
