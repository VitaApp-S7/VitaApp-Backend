package com.vitaquest.userservice.Domain.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CompleteMoodboosterDTO {

    private int points;
    private List<String> userIds;

}

