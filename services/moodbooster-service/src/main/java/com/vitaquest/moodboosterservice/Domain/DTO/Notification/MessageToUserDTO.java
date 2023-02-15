package com.vitaquest.moodboosterservice.Domain.DTO.Notification;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MessageToUserDTO {
    private String receiver;
    private String title;
    private String message;
}
