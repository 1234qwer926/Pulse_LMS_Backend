package com.LMS.Pulse.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

    // Inject the API key from application.properties
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Bean
    public Client GeminiClient() {
        // *** FIX: Use the builder pattern to initialize the client ***
        return Client.builder()
                .apiKey(geminiApiKey)
                .build();
    }
}
