package com.dtalk.ecosystem.services.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BlobService implements com.dtalk.ecosystem.services.BlobService {

    @Value("${azure.myblob.connectionstring}")
    private String connectionstring;
    @Value("${azure.myblob.container}")
    private String containerString;



    private BlobContainerClient containerClient() {
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectionstring).buildClient();
        BlobContainerClient container = serviceClient.getBlobContainerClient(containerString);
        return container;
    }

    public List<String> listFiles() {
        BlobContainerClient container = containerClient();
        List<String> list = new ArrayList<String>();
        for (BlobItem blobItem : container.listBlobs()) {
            list.add(blobItem.getName());
        }
        return list;
    }

    public ByteArrayOutputStream downloadFile(String blobitem) {
        BlobContainerClient containerClient = containerClient();
        BlobClient blobClient = containerClient.getBlobClient(blobitem);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        blobClient.download(os);
        return os;
    }

    public String storeFile(String filename, InputStream content, long length) {
        BlobClient client = containerClient().getBlobClient(filename);
        if (client.exists()) {
            System.out.println("The file was already located on azure");
        } else {
            client.upload(content, length);
        }

        return "File uploaded with success!";
    }


}
