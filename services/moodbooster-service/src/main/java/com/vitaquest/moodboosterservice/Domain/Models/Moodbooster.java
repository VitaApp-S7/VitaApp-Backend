package com.vitaquest.moodboosterservice.Domain.Models;


import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


import javax.persistence.*;

@Document("moodboosters")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Moodbooster {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type= "uuid-char")
    private String id;
    private String title;
    private String description;
    private Status status;
    private int points;

    @DocumentReference(lazy = true)
    private Category category;

}
