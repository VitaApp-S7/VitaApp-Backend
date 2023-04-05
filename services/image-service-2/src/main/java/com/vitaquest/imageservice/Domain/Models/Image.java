package com.example.imageservice.domain.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {

    private String id;
    private String name;
    private String url;
}
