package com.travelrecommendation.travelrecommendationsystem.web.controller;

import com.travelrecommendation.travelrecommendationsystem.dto.RegisterRequest;
import com.travelrecommendation.travelrecommendationsystem.exception.EmailAlreadyExistsException;
import com.travelrecommendation.travelrecommendationsystem.exception.InvalidCredentialsException;
import com.travelrecommendation.travelrecommendationsystem.exception.LocationNotFoundException;
import com.travelrecommendation.travelrecommendationsystem.exception.UsernameAlreadyExistsException;
import com.travelrecommendation.travelrecommendationsystem.model.User;
import com.travelrecommendation.travelrecommendationsystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("registerRequest", new RegisterRequest());

        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute RegisterRequest request,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.register(request);
            return "redirect:/login";
        } catch (UsernameAlreadyExistsException
                 | EmailAlreadyExistsException
                 | LocationNotFoundException e) {

            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        try {
            User user = userService.login(username, password);

            session.setAttribute("user", user);

            return "redirect:/";
        } catch (InvalidCredentialsException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }
}