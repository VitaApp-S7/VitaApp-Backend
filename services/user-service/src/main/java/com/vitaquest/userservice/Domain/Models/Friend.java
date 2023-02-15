package com.vitaquest.userservice.Domain.Models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("friends")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Friend {
    private String id;
    private String userId;
    private String friendId;
}
