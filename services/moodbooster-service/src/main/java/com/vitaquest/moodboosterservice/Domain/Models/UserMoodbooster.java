package com.vitaquest.moodboosterservice.Domain.Models;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Document("user_moodbooster")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMoodbooster {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type= "uuid-char")
    private String id;

    @ManyToOne
    private Moodbooster moodbooster;
    private UserMoodboosterStatus status;
    private Date completionDate;
    private List<String> userIds;
}
