package com.vitaquest.imageeservice.Domain.Service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobStorageException;
import com.azure.storage.blob.models.DeleteSnapshotsOptionType;
import com.azure.storage.blob.models.PublicAccessType;
import com.vitaquest.moodboosterservice.Database.Repository.IImageRepository;
import com.vitaquest.moodboosterservice.Domain.DTO.AddImageDTO;
import com.vitaquest.moodboosterservice.Domain.Models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
public class ImageService {
    private final IImageRepository repository;

    private final BlobContainerClient blobContainerClient;

    @Autowired
    public ImageService(IImageRepository repository, String connectionString, String containerName) {
        this.repository = repository;
        this.blobContainerClient = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();
    }

    public Image getImageById(String imageId) {
        Optional<Image> foundImage = repository.findById(imageId);
        if (foundImage.isEmpty()) {
            // throw not found exception
        }
        return foundImage.get();
    }

    public Image addImage(AddImageDTO dto) throws Exception {
        Image image = Image.builder()
                .id(UUID.randomUUID().toString())
                .name(dto.getName())
                .contentType(dto.getContentType())
                .size(dto.getSize())
                .build();

        // Create blob client and upload image data
        BlobClient blobClient = blobContainerClient.getBlobClient(image.getId());
        byte[] data = dto.getData();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(dto.getContentType());
        blobClient.upload(inputStream, data.length, true, headers, null, null);

        // Set access policy to allow anonymous access
        blobContainerClient.setAccessPolicy(PublicAccessType.CONTAINER);

        // Save image metadata to repository
        repository.save(image);
        return image;
    }

    public void deleteImage(String imageId) throws Exception {
        Image image = repository.findById(imageId).orElse(null);
        if (image == null) {
            // Throw not found exception
            return;
        }

        // Delete blob from storage
        BlobClient blobClient = blobContainerClient.getBlobClient(imageId);
        try {
            blobClient.delete(DeleteSnapshotsOptionType.INCLUDE);
        } catch (BlobStorageException e) {
            // Throw exception if delete operation failed
            throw new Exception("Failed to delete image from blob storage", e);
        }

        // Remove image metadata from repository
        repository.deleteById(imageId);
    }
}
