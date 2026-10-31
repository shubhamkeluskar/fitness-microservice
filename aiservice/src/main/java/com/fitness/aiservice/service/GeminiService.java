package com.fitness.aiservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public GeminiService() {
        this.webClient = WebClient.builder().build();
    }

    public String getAnswer(String question) {
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[]{
                                Map.of("text", question)
                        })
                }
        );

        try {
            String fullUri = geminiApiUrl + geminiApiKey;
            log.info("Sending request to Gemini API endpoint...");

            String response = webClient.post()
                    .uri(fullUri)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return response;
        } catch (Exception e) {
            log.error("Gemini API call failed with error: {}. Using fallback recommendation.", e.getMessage());
            // Fallback JSON-compatible text structure to prevent listener failures and UI freeze
            return "{\"candidates\":[{\"content\":{\"parts\":[{\"text\":\"Great workout! Maintain consistent pacing, stay well-hydrated, and ensure adequate post-workout rest for muscle recovery.\"}]}}]}";
        }
    }
}