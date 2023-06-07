package com.vitaquest.challengeservice.Domain.Models;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Document("challenges")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private String id;
    private String title;
    private String description;
    private List<String> moodboosterIds;
    private Date startDate;
    private Date endDate;
}

