package com.vitaquest.challengeservice.Domain.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CreateChallengeDTO {
    private String title;
    private String description;
    private List<String> moodboosterIds;
    private Date startDate;
    private Date endDate;
}

