package com.vitaquest.imageservice.Domain.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddImageDTO {
    private String name;
    private String contentType;
    private byte[] data;
    private long size;

    public AddImageDTO(String name, String contentType, byte[] data, long size) {
        this.name = name;
        this.contentType = contentType;
        this.data = data;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getData() {
        return data;
    }

    public long getSize() {
        return size;
    }
}