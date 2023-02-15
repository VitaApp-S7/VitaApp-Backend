package com.vitaquest.challengeservice.Domain.DTO;

import com.vitaquest.challengeservice.Domain.Models.Challenge;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AddUserChallengeDTO {

    private String initiatorId;
    private Challenge challenge;
    private List<String> userBuddys;
}
