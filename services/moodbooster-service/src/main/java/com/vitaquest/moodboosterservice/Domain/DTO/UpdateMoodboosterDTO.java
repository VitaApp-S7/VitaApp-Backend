package com.vitaquest.moodboosterservice.Domain.DTO;

import com.vitaquest.moodboosterservice.Domain.Models.Category;
import com.vitaquest.moodboosterservice.Domain.Models.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMoodboosterDTO {

    private String id;
    private String title;
    private String description;
    private Status status;
    private int points;
    private Category category;

}
