package com.vitaquest.imageservice.Domain.Service;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.vitaquest.imageservice.Domain.DTO.AddImageDTO;
import com.vitaquest.imageservice.Domain.Models.Image;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class ImageService {
    public BufferedImage getImageByID(String blobName) throws Exception {
        // Create a connection string to your Azure Storage account
        String connectionString = String.format("DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s;", accountName, accountKey);

        // Create a CloudStorageAccount object from the connection string
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);

        // Create a CloudBlobClient object from the CloudStorageAccount object
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

        // Get a reference to the container
        CloudBlobContainer container = blobClient.getContainerReference(containerName);

        // Get a reference to the blob
        CloudBlockBlob blob = container.getBlockBlobReference(blobName);

        System.out.println(String.format("Downloading blob '%s' from container '%s' in storage account '%s'...", blobName, containerName, accountName));

        // Download the blob to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blob.download(outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        System.out.println(String.format("Blob '%s' downloaded successfully from container '%s' in storage account '%s'.", blobName, containerName, accountName));

        // Convert the byte array to a BufferedImage object
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        return bufferedImage;
    }


    public Image addImage(AddImageDTO dto) throws Exception {
        // Create a connection string to your Azure Storage account
        String connectionString = String.format("DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s;", accountName, accountKey);

        // Create a CloudStorageAccount object from the connection string
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);

        // Create a CloudBlobClient object from the CloudStorageAccount object
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

        // Get a reference to the container
        CloudBlobContainer container = blobClient.getContainerReference(containerName);

        // Generate a unique identifier for the blob
        //String blobName = UUID.randomUUID().toString();
        String blobName = dto.getName();
        System.out.println(String.format("Creating blob '%s' in container '%s' in storage account '%s'...", blobName, containerName, accountName));

        // Create a CloudBlockBlob object and set the blob name
        CloudBlockBlob blob = container.getBlockBlobReference(blobName);

        // Convert the base64 encoded string to a byte array
        //byte[] imageBytes = Base64.getDecoder().decode(dto.getData());

        // Upload the image byte array to the blob
        //ByteArrayInputStream inputStream = new ByteArrayInputStream(dto.getData());
        //blob.upload(inputStream, dto.getData().length);

        byte[] decodedImageData = Base64.getDecoder().decode(dto.getData());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedImageData);
        blob.upload(inputStream, decodedImageData.length);


        System.out.println(String.format("Blob '%s' uploaded successfully to container '%s' in storage account '%s'.", blobName, containerName, accountName));

        // Set the content type of the blob based on the image format
        //String contentType = "image/" + dto.getContentType().toLowerCase();
        //blob.getProperties().setContentType(contentType);
        blob.uploadProperties();

        // Create an Image object and set the properties
        Image newImage = new Image();
        newImage.setId(blobName);
        //newImage.setUrl(blob.getUri().toString());
        newImage.setUrl(blob.getUri().toString());
        //image.setName(dto.getName());
        //image.setContentType(dto.getContentType());
        newImage.setSize(dto.getSize());
        //image.setContentType(contentType);

        System.out.println(String.format("Blob '%s' created successfully in container '%s' in storage account '%s'.", blobName, containerName, accountName));
        return newImage;
    }



    public void deleteImageByID(String imageId) throws Exception {
    }


}
