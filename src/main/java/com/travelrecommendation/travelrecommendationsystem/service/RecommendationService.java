package com.travelrecommendation.travelrecommendationsystem.service;

import com.travelrecommendation.travelrecommendationsystem.dto.RecommendationRequest;
import com.travelrecommendation.travelrecommendationsystem.dto.RecommendationResponse;
import com.travelrecommendation.travelrecommendationsystem.model.User;

public interface RecommendationService {

    RecommendationResponse recommend(RecommendationRequest request, User user);
}