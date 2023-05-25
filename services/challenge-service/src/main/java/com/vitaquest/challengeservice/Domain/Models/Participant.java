package com.vitaquest.challengeservice.Domain.Models;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
    private String name;
    private String userId;
    private int score;
}

