package com.vitaquest.userservice.Domain.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FriendDTO {
    private String id;
    private String userId;
    private String name;
}
