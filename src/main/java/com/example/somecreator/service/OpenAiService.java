package com.example.somecreator.service;

import com.example.somecreator.dto.ChatCompletionRequest;
import com.example.somecreator.dto.ChatCompletionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OpenAiService {

    public static final Logger logger = LoggerFactory.getLogger(OpenAiService.class);

    @Value("$app.api-key")
    private String API_KEY;


    public final static String URL = "https://api.openai.com/v1/chat/completions";
    public final static String MODEL = "gpt-3.5-turbo";
    public final static double TEMPERATURE = 0.8;
    public final static int MAX_TOKENS = 300;
    public final static double FREQUENCY_PENALTY = 0.0;
    public final static double PRESENCE_PENALTY = 0.0;
    public final static double TOP_P = 1.0;


    private final WebClient webClient;

    public OpenAiService() {
        this.webClient = WebClient.create();
    }

    public OpenAiService(WebClient client) {
        this.webClient = client;
    }


    public String generateContent(String topic) {
        try {
            ChatCompletionRequest request = new ChatCompletionRequest();
            request.setModel(MODEL);
            request.setTemperature(TEMPERATURE);
            request.setMax_tokens(MAX_TOKENS);
            request.setFrequency_penalty(FREQUENCY_PENALTY);
            request.setPresence_penalty(PRESENCE_PENALTY);
            request.setTop_p(TOP_P);

            // Create a message for the assistant
            ChatCompletionRequest.Message assistantMessage = new ChatCompletionRequest.Message("system", "You are an assistant that will help the user generate good texts for social media content.");
            request.getMessages().add(assistantMessage);

            // Create a message for the user with the provided topic
            ChatCompletionRequest.Message userMessage = new ChatCompletionRequest.Message("user", "User: " + topic);
            request.getMessages().add(userMessage);

            // Use WebClient to send the request to the OpenAI API
            ChatCompletionResponse response = webClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(request))
                    .retrieve()
                    .bodyToMono(ChatCompletionResponse.class)
                    .block();


        } catch (Exception e) {
            logger.error("An error occurred while sending the request to OpenAI API", e);
            return "An error occurred";
        }
        return topic;
    }


}

