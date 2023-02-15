package com.vitaquest.userservice.Domain.Models;

import com.bol.secure.Encrypted;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document("users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String name;
    @Encrypted
    private String email;
    private Integer mood = 10;
    private Integer pointsEarned;
    @Encrypted
    private String expoToken;

    private Boolean modalVisable;

    private String date;

    public User(String id, String name, String email, Integer mood, Integer pointsEarned) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mood = mood;
        this.pointsEarned = pointsEarned;
    }
}