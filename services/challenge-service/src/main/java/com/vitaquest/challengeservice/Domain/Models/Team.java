package com.vitaquest.challengeservice.Domain.Models;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Document("teams")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private String id;
    private String name;
    private String challengeId;
    private String reward;
    private int score;
    private List<Participant> participants;
}
