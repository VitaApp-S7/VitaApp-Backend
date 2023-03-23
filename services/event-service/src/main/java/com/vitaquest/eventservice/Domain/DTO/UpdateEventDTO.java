package com.vitaquest.eventservice.Domain.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UpdateEventDTO {
    private String id;
    private String title;
    private String description;
    private Date date;
    private List<String> userIds;

}
