package com.travelrecommendation.travelrecommendationsystem.web.controller;

import com.travelrecommendation.travelrecommendationsystem.dto.RecommendationRequest;
import com.travelrecommendation.travelrecommendationsystem.dto.RecommendationResponse;
import com.travelrecommendation.travelrecommendationsystem.model.User;
import com.travelrecommendation.travelrecommendationsystem.service.FavoriteService;
import com.travelrecommendation.travelrecommendationsystem.service.RecommendationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final FavoriteService favoriteService;

    public RecommendationController(
            RecommendationService recommendationService,
            FavoriteService favoriteService) {

        this.recommendationService = recommendationService;
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public String getRecommendationForm(Model model) {
        model.addAttribute("recommendationRequest", new RecommendationRequest());

        return "recommendation-form";
    }

    @PostMapping
    public String getRecommendations(
            @ModelAttribute RecommendationRequest request,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            model.addAttribute(
                    "error",
                    "Please login to get travel recommendations."
            );

            return "login";
        }

        RecommendationResponse response =
                recommendationService.recommend(request, user);

        model.addAttribute("recommendations", response.getRecommendations());

        Set<Long> favoriteDestinationIds = favoriteService.findByUserId(user.getId())
                .stream()
                .map(favorite -> favorite.getDestination().getId())
                .collect(Collectors.toSet());

        model.addAttribute("favoriteDestinationIds", favoriteDestinationIds);

        return "recommendations";
    }
}