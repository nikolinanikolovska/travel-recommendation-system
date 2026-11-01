package com.travelrecommendation.travelrecommendationsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatbotRequest {

    private String message;

    public ChatbotRequest(String message) {
        this.message = message;
    }
}