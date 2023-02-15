package com.vitaquest.feedservice.Domain.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AllUserNotificationDTO {
    private String title;
    private String message;
}

