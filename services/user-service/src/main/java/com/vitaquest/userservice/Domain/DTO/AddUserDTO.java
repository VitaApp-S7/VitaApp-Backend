package com.vitaquest.userservice.Domain.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddUserDTO {
    private String id;
    private String name;
    private String email;
}
