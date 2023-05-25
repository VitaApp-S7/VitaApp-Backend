package com.vitaquest.imageservice.Domain.Service;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
public class ImageService {
    @Value("${AZURE_STORAGE_ACCOUNT_KEY}")
    private String accountKey;

    @Value("${AZURE_STORAGE_CONTAINER_NAME}")
    private String containerName;

    @Value("${AZURE_STORAGE_ACCOUNT_NAME}")
    private String accountName;

    public String addImage(byte[] imageBytes) throws Exception {
        // Create a connection string to your Azure Storage account
        String connectionString = String.format("DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s;", accountName, accountKey);

        // Create a CloudStorageAccount object from the connection string
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);

        // Create a CloudBlobClient object from the CloudStorageAccount object
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

        // Get a reference to the container
        CloudBlobContainer container = blobClient.getContainerReference(containerName);

        String generatedName = UUID.randomUUID().toString();

        // Create a CloudBlockBlob object and set the blob name
        CloudBlockBlob blob = container.getBlockBlobReference(generatedName);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        blob.upload(inputStream, imageBytes.length);

        System.out.printf("Blob '%s' uploaded successfully to container '%s' in storage account '%s'.%n", generatedName, containerName, accountName);

        blob.uploadProperties();

        //return the image url
        return blob.getUri().toString();
    }

    public void deleteImageByID(String imageId) throws Exception {

    }
}
