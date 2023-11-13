package com.example.somecreator.api;

import com.example.somecreator.dto.Generate;
import com.example.somecreator.service.OpenAiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/generate")
public class OpenAiController {

    private OpenAiService service;
    final static String SYSTEM_MESSAGE ="You are an assistant that will help the user generate good texts for social media content,"+"" +
            " you will also generate the texts with correct grammar.";

    public OpenAiController(OpenAiService service){
        this.service = service;
    }
    @GetMapping
    public Generate generateContent(@RequestParam String topic) {
        return  service.createChatCompletionRequest(topic, SYSTEM_MESSAGE);
    }
}