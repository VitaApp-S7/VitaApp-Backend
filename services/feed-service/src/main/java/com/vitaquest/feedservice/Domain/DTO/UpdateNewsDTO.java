package com.vitaquest.feedservice.Domain.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UpdateNewsDTO {
    private String id;
    private String title;
    private String description;
    private Date date;
}
