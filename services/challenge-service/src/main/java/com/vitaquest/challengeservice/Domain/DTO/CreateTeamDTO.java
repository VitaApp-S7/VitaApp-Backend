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
    private String name;
    private String challengeId;
    private String reward;
    public int getScore(){
        int score = 0;
        for (int i = 0; i < participants.size(); i++) {
            score += participants.get(i).getScore();
        }
        return score;
    };
    private List<Participant> participants;
}
