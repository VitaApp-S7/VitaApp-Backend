package com.vitaquest.moodboosterservice.Domain.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserMoodBoosterStats {
    @Id
    private UserMoodboosterStatsInfo info;
    private Integer count;

}
