package com.vitaquest.challengeservice.Domain.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateChallengeDTO {

    private String id;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private String activeTime;
    private String reward;

}
