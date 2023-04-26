package com.vitaquest.challengeservice.Domain.DTO;

import com.vitaquest.challengeservice.Domain.Models.Participant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateTeamDTO {
    private String id;
    private String name;
    private String challengeId;
    private String reward;
    private int score;
    private List<Participant> participants;
}
