package com.vitaquest.badgeservice.Domain.DTO;


import com.vitaquest.badgeservice.Domain.Models.BadgeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class AddBadgeDTO{
    private String name;
    private String description;
    private String activityId;
    private int bronzeTreshold;
    private int silverTreshold;
    private int goldTreshold;
    private BadgeStatus status;
    private Binary imageSet;// don't know if it must be a array or just normal.
}
