package com.example.somecreator.service;

import com.example.somecreator.dto.ChatCompletionRequest;
import com.example.somecreator.dto.ChatCompletionResponse;
import com.example.somecreator.dto.Generate;
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
        String json = "{\n" +
                "    \"model\": \"gpt-3.5-turbo\",\n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"role\": \"system\",\n" +
                "            \"content\": \"You are an assistant that will help the user generate good texts for social media content. The user should provide the tone and topic. you will also generate the texts with correct grammar.\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"role\": \"user\",\n" +
                "            \"content\": \"I need text for my instagram post where I'm swimming with dolphins\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"role\": \"assistant\",\n" +
                "            \"content\": \"\\\"Feeling truly blessed as I dive into the crystal-clear waters of (location) and share this unforgettable experience with these beautiful dolphins \uD83D\uDC2C✨ It's moments like these that reaffirm my love for marine life and remind me of the incredible wonders our oceans hold. Swimming alongside these graceful creatures was like a dream come true! \uD83D\uDE0D\uD83D\uDC99 #SwimmingWithDolphins #BucketListExperience #OceanAdventures #NatureLovers #DolphinEncounter\\\"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"role\": \"assistant\",\n" +
                "            \"content\": \"\\\"\uD83C\uDF0A✨ Jumping into a world of magic and wonder, surrounded by the joyful presence of these incredible dolphins! \uD83D\uDC2C\uD83D\uDC99 Today, I had the incredible opportunity to swim alongside these intelligent beings and witness their playful nature up close. The bond we share with the ocean and its inhabitants is truly something special. Grateful for this unforgettable experience! \uD83D\uDE4F❤\uFE0F #SwimmingWithDolphins #MagicalEncounter #OceanExploration #GratefulHeart #NatureEnthusiast\\\"\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"temperature\": 1,\n" +
                "    \"max_tokens\": 256,\n" +
                "    \"top_p\": 1,\n" +
                "    \"frequency_penalty\": 0,\n" +
                "    \"presence_penalty\": 0\n" +
                "}\n";
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
