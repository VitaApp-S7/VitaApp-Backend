package com.vitaquest.challengeservice.Domain.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AddPointsDTO {
    private String teamId;
    private List<String> userIds;
    private int points;
}
