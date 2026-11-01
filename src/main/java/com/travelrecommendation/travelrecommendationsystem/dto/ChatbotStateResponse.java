package com.travelrecommendation.travelrecommendationsystem.dto;

import com.travelrecommendation.travelrecommendationsystem.model.Destination;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatbotStateResponse {

    private List<ChatMessage> messages;
    private List<Destination> recommendations;

    public ChatbotStateResponse(
            List<ChatMessage> messages,
            List<Destination> recommendations) {

        this.messages = messages;
        this.recommendations = recommendations;
    }
}
