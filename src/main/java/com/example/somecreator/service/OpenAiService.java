package com.example.somecreator.service;

import com.example.somecreator.dto.*;
import com.example.somecreator.dto.ChatCompletionResponse;
import com.example.somecreator.dto.ChatCompletionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OpenAiService {

    @Value("${app.api-key}")
    private String API_KEY;

    private static final String URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-3.5-turbo";
    private static final double TEMPERATURE = 0.8;
    private static final int MAX_TOKENS = 300;
    private static final double FREQUENCY_PENALTY = 0.0;
    private static final double PRESENCE_PENALTY = 0.0;
    private static final double TOP_P = 1.0;

    private final WebClient webClient;

    public OpenAiService() {
        this.webClient = WebClient.create();
    }

    public Generate createChatCompletionRequest(String userPrompt, String systemMessage) {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(MODEL);
        request.setTemperature(TEMPERATURE);
        request.setMax_tokens(MAX_TOKENS);
        request.setTop_p(TOP_P);
        request.setFrequency_penalty(FREQUENCY_PENALTY);
        request.setPresence_penalty(PRESENCE_PENALTY);
        request.getMessages().add(new ChatCompletionRequest.Message("system", systemMessage));
        request.getMessages().add(new ChatCompletionRequest.Message("user", userPrompt));

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(request);
            ChatCompletionResponse response = webClient.post()
                    .uri(URL)
                    .header("Authorization", "Bearer " + API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON) //accepts JSON for the request
                    .body(BodyInserters.fromValue(json))
                    .retrieve()
                    .bodyToMono(ChatCompletionResponse.class)
                    .block();

            String responseMsg = response.getChoices().get(0).getMessage().getContent();
            return new Generate(responseMsg);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}