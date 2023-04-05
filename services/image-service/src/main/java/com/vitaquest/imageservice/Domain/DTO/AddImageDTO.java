package com.vitaquest.imageservice.Domain.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddImageDTO {
    private String name;
    private String url;
}