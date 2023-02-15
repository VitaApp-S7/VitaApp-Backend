package com.vitaquest.challengeservice.Domain.Models;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;


import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;


@Document("challenges")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Challenge {

    // Todo: add requirements for challenge completion
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type= "uuid-char")
    private String id;
    private String title;
    private String description;
    private String activityId;
    private int goal;
    private Date startDate;
    private Date endDate;
    // Could bew changed to duration if challenges are less than a day
    // Todo: Add handler to convert to Period if challenges are longer.
    private Duration activeTime;
    private String Reward;


}
