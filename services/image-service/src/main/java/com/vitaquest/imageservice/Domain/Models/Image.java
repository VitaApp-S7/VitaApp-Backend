package com.vitaquest.imageservice.Domain.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {
    private String id;
    private String name;
    private String url;
    private String contentType;
    private byte[] data;
    private long size;
}
