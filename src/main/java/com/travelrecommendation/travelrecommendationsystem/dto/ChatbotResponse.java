package com.travelrecommendation.travelrecommendationsystem.dto;

import com.travelrecommendation.travelrecommendationsystem.model.Destination;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatbotResponse {

    private String message;
    private List<Destination> recommendations;

    public ChatbotResponse(String message) {
        this.message = message;
    }

    public ChatbotResponse(
            String message,
            List<Destination> recommendations) {

        this.message = message;
        this.recommendations = recommendations;
    }
}