package com.vitaquest.moodboosterservice.Domain.DTO;

import com.vitaquest.moodboosterservice.Domain.Models.Moodbooster;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CompleteMoodboosterDTO {

    private int points;
    private List<String> userIds;

}
