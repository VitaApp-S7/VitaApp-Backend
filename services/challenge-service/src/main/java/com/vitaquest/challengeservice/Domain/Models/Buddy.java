package com.vitaquest.challengeservice.Domain.Models;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Document("buddies")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Buddy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type= "uuid-char")
    private String id;
    private String userId;
    private BuddyStatus status;
}
