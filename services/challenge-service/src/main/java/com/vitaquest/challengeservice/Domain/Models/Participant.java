package com.vitaquest.challengeservice.Domain.Models;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Participant {
    private String name;
    private String userId;
    private int score;
}

