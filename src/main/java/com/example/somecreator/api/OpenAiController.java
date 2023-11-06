package com.example.somecreator.api;


import com.example.somecreator.service.OpenAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class OpenAiController {

    private OpenAiService service;
    final static String SYSTEM_MESSAGE ="You are an assistant that will help the user generate good texts for social media content,"+"" +
            " you will also generate the texts with correct grammar.";

    public OpenAiController(OpenAiService service){
        this.service = service;
    }

    @PostMapping("/generate-content")
    public ResponseEntity<String> generateContent(@RequestBody Map<String, String> request) {
        String topic = request.get("topic");

        // Call your OpenAI service to generate content based on the topic
        String generatedContent = service.generateContent(topic);

        // Return the generated content as a response
        return ResponseEntity.ok(generatedContent);
    }

}
