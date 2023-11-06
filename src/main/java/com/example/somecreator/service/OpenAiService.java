package com.example.somecreator.service;

import com.example.somecreator.dto.ChatCompletionRequest;
import com.example.somecreator.dto.ChatCompletionResponse;
import com.example.somecreator.dto.Generate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OpenAiService {

    private static final Logger logger = LoggerFactory.getLogger(OpenAiService.class);

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

    public Generate generateContent(String userPrompt, String systemMessage) {
        try {
            ChatCompletionRequest request = createChatCompletionRequest(userPrompt, systemMessage);

            // Use WebClient to send the request to the OpenAI API
            ChatCompletionResponse response = sendRequestToOpenAI(request);

            // Return the response from OpenAI as a Generate object
            return new Generate(getGeneratedTextFromResponse(response));
        } catch (Exception e) {
            handleRequestError(e);
            return null; // Return null or an appropriate error response object
        }
    }

    private ChatCompletionRequest createChatCompletionRequest(String userPrompt, String systemMessage) {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(MODEL);
        request.setTemperature(TEMPERATURE);
        request.setMax_tokens(MAX_TOKENS);
        request.setTop_p(TOP_P);
        request.setFrequency_penalty(FREQUENCY_PENALTY);
        request.setPresence_penalty(PRESENCE_PENALTY);
        request.getMessages().add(new ChatCompletionRequest.Message("system", systemMessage));
        request.getMessages().add(new ChatCompletionRequest.Message("user", userPrompt));
        return request;
    }

    private ChatCompletionResponse sendRequestToOpenAI(ChatCompletionRequest request) {
        return webClient.post()
                .uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(ChatCompletionResponse.class)
                .block();
    }

    private String getGeneratedTextFromResponse(ChatCompletionResponse response) {
        return response.getChoices().get(0).getMessage().getContent();
    }

    private void handleRequestError(Exception e) {
        logger.error("An error occurred while sending the request to OpenAI API", e);
        // Handle the error appropriately, e.g., return an error response
        // Instead of returning a string, consider returning an error object
    }
}
