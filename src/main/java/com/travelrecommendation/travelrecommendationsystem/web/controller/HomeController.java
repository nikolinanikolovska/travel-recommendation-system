package com.travelrecommendation.travelrecommendationsystem.web.controller;

import com.travelrecommendation.travelrecommendationsystem.model.Destination;
import com.travelrecommendation.travelrecommendationsystem.model.User;
import com.travelrecommendation.travelrecommendationsystem.repository.DestinationRepository;
import com.travelrecommendation.travelrecommendationsystem.service.FavoriteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final DestinationRepository destinationRepository;
    private final FavoriteService favoriteService;

    public HomeController(
            DestinationRepository destinationRepository,
            FavoriteService favoriteService) {

        this.destinationRepository = destinationRepository;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/")
    public String getHomePage(
            Model model,
            HttpSession session) {

        List<Destination> destinations =
                destinationRepository.findAll();

        model.addAttribute(
                "destinations",
                destinations
        );

        User user =
                (User) session.getAttribute("user");

        Set<Long> favoriteDestinationIds =
                user == null
                        ? Collections.emptySet()
                        : favoriteService.findByUserId(user.getId())
                        .stream()
                        .map(favorite ->
                                favorite.getDestination().getId())
                        .collect(Collectors.toSet());

        model.addAttribute(
                "favoriteDestinationIds",
                favoriteDestinationIds
        );

        return "home";
    }
}