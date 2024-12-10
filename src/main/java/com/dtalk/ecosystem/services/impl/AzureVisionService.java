package com.dtalk.ecosystem.services.impl;



import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AzureVisionService {

    private final String API_KEY = "2XdfUdUyPOe6kwEZondXHjfgeM5h2P8EzGm4pyHY3nA9TLSPVBMJJQQJ99ALACYeBjFXJ3w3AAAFACOGbJhn";
    private final String API_ENDPOINT = "https://stalk.cognitiveservices.azure.com/vision/v3.2/analyze";




    public String describeImage(String imageUrl) throws Exception {
        String visionUrl = API_ENDPOINT + "?visualFeatures=Description";

        // Build request payload
        Map<String, String> payload = new HashMap<>();
        payload.put("url", imageUrl);

        ObjectMapper mapper = new ObjectMapper();
        String payloadJson = mapper.writeValueAsString(payload);

        // HTTP client setup
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(visionUrl);
            post.setHeader("Ocp-Apim-Subscription-Key", API_KEY);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(payloadJson));

            try (CloseableHttpResponse response = client.execute(post)) {
                if (true) {
                    Map<?, ?> result = mapper.readValue(response.getEntity().getContent(), Map.class);
                    System.out.println(result);
                    return mapper.writeValueAsString(result.get("description"));
                } else {
                    throw new RuntimeException("Error: " );
                }
            }
        }
    }
}
