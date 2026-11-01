package com.travelrecommendation.travelrecommendationsystem.service;

import com.travelrecommendation.travelrecommendationsystem.dto.ChatbotRequest;
import com.travelrecommendation.travelrecommendationsystem.dto.ChatbotResponse;
import com.travelrecommendation.travelrecommendationsystem.model.User;

import java.util.Map;

public interface ChatbotService {

    ChatbotResponse chat(
            ChatbotRequest request,
            User user,
            Map<String, Object> conversation
    );
}