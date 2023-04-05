package com.vitaquest.imageservice.Domain.Service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobStorageException;
import com.azure.storage.blob.models.DeleteSnapshotsOptionType;
import com.azure.storage.blob.models.PublicAccessType;
import com.vitaquest.imageservice.Domain.DTO.AddImageDTO;
import com.vitaquest.imageservice.Domain.Models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {
    private final BlobContainerClient blobContainerClient;

    @Autowired
    public ImageService(String connectionString, String containerName) {
        this.blobContainerClient = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();
    }

    public Image getImageById(String imageId) throws Exception {
        BlobClient blobClient = blobContainerClient.getBlobClient(imageId);
        if (!blobClient.exists()) {
            // throw not found exception
            throw new Exception("Image not found");
        }
        return Image.builder()
                .id(imageId)
                .name(blobClient.getBlobName())
                .build();
    }

    public Image addImage(AddImageDTO dto) throws Exception {
        String imageId = UUID.randomUUID().toString();
        BlobClient blobClient = blobContainerClient.getBlobClient(imageId);
        byte[] data = dto.getData();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(dto.getContentType());
        blobClient.upload(inputStream, data.length, true, headers, null, null);
        //blobContainerClient.setAccessPolicy(PublicAccessType.CONTAINER);
        return Image.builder()
                .id(imageId)
                .name(dto.getName())
                .contentType(dto.getContentType())
                .size(dto.getSize())
                .build();
    }

    public void deleteImage(String imageId) throws Exception {
        BlobClient blobClient = blobContainerClient.getBlobClient(imageId);
        if (!blobClient.exists()) {
            // throw not found exception
            throw new Exception("Image not found");
        }
        try {
            blobClient.delete(DeleteSnapshotsOptionType.INCLUDE);
        } catch (BlobStorageException e) {
            // Throw exception if delete operation failed
            throw new Exception("Failed to delete image from blob storage", e);
        }
    }
}
