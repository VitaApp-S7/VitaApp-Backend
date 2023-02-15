package com.vitaquest.challengeservice.Domain.Models;
import lombok.*;
import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Document("user_challenges")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type= "uuid-char")
    private String id;
    private String initiatorId;
    private String challengeId;
    private ChallengeStatus status;
    private int progress;

    @DocumentReference(lazy = true)
    private List<Buddy> userBuddys;

    private LocalDate startDate;

    public boolean inviteBuddy(String userId){
        if(this.userBuddys.stream().filter(b -> b.getUserId().equals(userId)).findAny().orElse(null) == null){
            Buddy buddy = new Buddy();
            buddy.setUserId(userId);
            buddy.setStatus(BuddyStatus.INVITED);
            this.userBuddys.add(buddy);
            return true;
        }
        return false;
    }

    public boolean buddyAccept(String userId) {
        Buddy buddy = this.userBuddys.stream().filter(b -> b.getUserId().equals(userId)).findAny().orElse(null);
        if( buddy != null){
            buddy.setStatus(BuddyStatus.ACCEPTED);

            return true;
        }
        return false;
    }

    public boolean buddyReject(String userId) {
        Buddy buddy = this.userBuddys.stream().filter(b -> b.getUserId().equals(userId)).findAny().orElse(null);
        if( buddy != null){
            buddy.setStatus(BuddyStatus.REJECTED);

            return true;
        }
        return false;
    }

}
