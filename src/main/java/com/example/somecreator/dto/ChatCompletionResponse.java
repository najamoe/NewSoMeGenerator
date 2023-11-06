package com.example.somecreator.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatCompletionResponse {

        private String id;
        private String object;
        private long created;
        private String model;

        @Getter
        @Setter
        public static class Message {
            private String role;
            private String content;
        }




}
