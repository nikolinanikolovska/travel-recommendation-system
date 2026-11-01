package com.travelrecommendation.travelrecommendationsystem.web.controller;

import com.travelrecommendation.travelrecommendationsystem.exception.FavoriteAlreadyExistsException;
import com.travelrecommendation.travelrecommendationsystem.model.User;
import com.travelrecommendation.travelrecommendationsystem.service.FavoriteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public String getFavorites(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "Please login to view your favorites.");
            return "login";
        }

        model.addAttribute("favorites",
                favoriteService.findByUserId(user.getId()));

        return "favorites";
    }

    @PostMapping("/add")
    public String addFavorite(
            @RequestParam Long destinationId,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "Please login to manage favorites.");
            return "login";
        }

        try {
            favoriteService.addFavorite(user.getId(), destinationId);
            return "redirect:/recommendations";
        } catch (FavoriteAlreadyExistsException e) {
            model.addAttribute("favorites",
                    favoriteService.findByUserId(user.getId()));
            model.addAttribute("error", e.getMessage());

            return "favorites";
        }
    }

    @PostMapping("/remove")
    public String removeFavorite(
            @RequestParam Long favoriteId,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "Please login to manage favorites.");
            return "login";
        }

        favoriteService.removeFavorite(favoriteId, user.getId());

        return "redirect:/favorites";
    }
}