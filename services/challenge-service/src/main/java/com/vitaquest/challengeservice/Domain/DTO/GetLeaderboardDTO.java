package com.vitaquest.challengeservice.Domain.DTO;

import com.vitaquest.challengeservice.Domain.Models.Participant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetLeaderboardDTO {
    private List<Participant> participants;
}
