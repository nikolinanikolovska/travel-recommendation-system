package com.travelrecommendation.travelrecommendationsystem.web.controller;

import com.travelrecommendation.travelrecommendationsystem.model.Destination;
import com.travelrecommendation.travelrecommendationsystem.service.DestinationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(
            DestinationService destinationService) {

        this.destinationService = destinationService;
    }

    @GetMapping
    public String getAllDestinations(
            Model model) {

        var destinations =
                destinationService.findAll();

        model.addAttribute(
                "destinations",
                destinations
        );

        return "destinations";
    }

    @GetMapping("/{id}")
    public String getDestinationById(
            @PathVariable Long id,
            Model model) {

        Destination destination =
                destinationService.findById(id);

        model.addAttribute(
                "destination",
                destination
        );

        return "destination";
    }

    @GetMapping("/create")
    public String getCreateDestinationForm(
            Model model) {

        model.addAttribute(
                "destination",
                new Destination()
        );

        return "destination-form";
    }

    @PostMapping
    public String saveDestination(
            @ModelAttribute Destination destination) {

        destinationService.save(destination);

        return "redirect:/destinations";
    }

    @GetMapping("/edit/{id}")
    public String getEditDestinationForm(
            @PathVariable Long id,
            Model model) {

        Destination destination =
                destinationService.findById(id);

        model.addAttribute(
                "destination",
                destination
        );

        return "destination-form";
    }

    @PostMapping("/{id}")
    public String updateDestination(
            @PathVariable Long id,
            @ModelAttribute Destination destination) {

        destinationService.update(
                id,
                destination
        );

        return "redirect:/destinations";
    }

    @PostMapping("/delete/{id}")
    public String deleteDestination(
            @PathVariable Long id) {

        destinationService.deleteById(id);

        return "redirect:/destinations";
    }
}