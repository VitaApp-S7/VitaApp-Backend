package com.vitaquest.userservice.Domain.DTO.Notification;

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
