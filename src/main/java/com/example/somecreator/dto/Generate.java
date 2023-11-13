package com.example.somecreator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Generate {
    String answer;
    List<Map<String, String>> messages;

    public Generate(String answer) {
        this.answer = answer;
    }
    public Generate(String answer, List<Map<String,String>> messages) {
        this.answer = answer;
        this.messages = messages;
    }
}