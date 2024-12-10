package com.dtalk.ecosystem.controllers;

import com.dtalk.ecosystem.services.impl.AzureVisionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class VisionController {

    private AzureVisionService azureVisionService;

    @GetMapping("/analyze")
    public String analyzeImage(@RequestParam String imageUrl) {
        try {
            return azureVisionService.describeImage(imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error analyzing image: " + e.getMessage();
        }
    }
}

