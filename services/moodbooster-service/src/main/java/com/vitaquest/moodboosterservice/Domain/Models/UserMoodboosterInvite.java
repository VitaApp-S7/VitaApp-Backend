package com.vitaquest.moodboosterservice.Domain.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("moodboosterInvites")
public class UserMoodboosterInvite {
    @Id
    private String inviteId;
    private String userMoodboosterId;
    private String inviterName;
    private String inviterId;
    private String invitedUserId;
    private String moodboosterName;
    private String moodboosterDescription;
}
