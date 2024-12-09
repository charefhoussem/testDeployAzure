package com.dtalk.ecosystem.services.impl;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AzureVisionService {

    private final String subscriptionKey = "2XdfUdUyPOe6kwEZondXHjfgeM5h2P8EzGm4pyHY3nA9TLSPVBMJJQQJ99ALACYeBjFXJ3w3AAAFACOGbJhn";
    private final String endpoint = "https://stalk.cognitiveservices.azure.com/";

    public String analyzeImage(String imageUrl) throws Exception {
        String uri = endpoint + "/vision/v3.2/analyze?visualFeatures=Description,Tags";

        // Create HTTP client
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(uri);

            // Set headers
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Set request body
            StringEntity requestBody = new StringEntity("{\"url\":\"" + imageUrl + "\"}");
            request.setEntity(requestBody);

            // Execute request
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Parse the response
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonResponse = mapper.readTree(entity.getContent());
                return jsonResponse.toPrettyString();
            }
        }

        return null;
    }
}
