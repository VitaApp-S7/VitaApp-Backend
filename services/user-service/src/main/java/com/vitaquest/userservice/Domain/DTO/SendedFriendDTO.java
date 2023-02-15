package com.vitaquest.userservice.Domain.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SendedFriendDTO {
    private String id;
    private String friendId;
    private String name;
}
