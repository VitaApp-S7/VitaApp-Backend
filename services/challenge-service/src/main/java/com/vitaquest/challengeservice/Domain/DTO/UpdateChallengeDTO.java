package com.vitaquest.challengeservice.Domain.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UpdateChallengeDTO {
    private String id;
    private String title;
    private String description;
    private List<String> moodboosterIds;
    private Date startDate;
    private Date endDate;
}
