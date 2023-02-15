package com.vitaquest.userservice.Domain.DTO.Notification;

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
