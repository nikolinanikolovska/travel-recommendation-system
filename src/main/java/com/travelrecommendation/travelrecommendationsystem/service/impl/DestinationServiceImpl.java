package com.travelrecommendation.travelrecommendationsystem.service.impl;

import com.travelrecommendation.travelrecommendationsystem.exception.DestinationNotFoundException;
import com.travelrecommendation.travelrecommendationsystem.model.Coordinates;
import com.travelrecommendation.travelrecommendationsystem.model.Destination;
import com.travelrecommendation.travelrecommendationsystem.repository.DestinationRepository;
import com.travelrecommendation.travelrecommendationsystem.service.DestinationService;
import com.travelrecommendation.travelrecommendationsystem.service.GeocodingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;
    private final GeocodingService geocodingService;

    public DestinationServiceImpl(
            DestinationRepository destinationRepository,
            GeocodingService geocodingService) {

        this.destinationRepository = destinationRepository;
        this.geocodingService = geocodingService;
    }

    @Override
    public List<Destination> findAll() {
        return destinationRepository.findAll();
    }

    @Override
    public Destination findById(Long id) {
        return destinationRepository.findById(id)
                .orElseThrow(DestinationNotFoundException::new);
    }

    @Override
    public Destination save(Destination destination) {
        updateCoordinates(destination);

        return destinationRepository.save(destination);
    }

    @Override
    public Destination update(Long id, Destination destination) {
        Destination existingDestination = findById(id);

        existingDestination.setName(destination.getName());
        existingDestination.setCountry(destination.getCountry());
        existingDestination.setImageUrl(destination.getImageUrl());
        existingDestination.setDescription(destination.getDescription());
        existingDestination.setAverageDailyCost(destination.getAverageDailyCost());
        existingDestination.setRecommendedDuration(destination.getRecommendedDuration());
        existingDestination.setTravelType(destination.getTravelType());
        existingDestination.setSeason(destination.getSeason());

        updateCoordinates(existingDestination);

        return destinationRepository.save(existingDestination);
    }

    @Override
    public void deleteById(Long id) {
        destinationRepository.deleteById(id);
    }

    private void updateCoordinates(Destination destination) {
        Coordinates coordinates = geocodingService.getCoordinates(
                destination.getName() + ", " + destination.getCountry()
        );

        destination.setLatitude(coordinates.getLatitude());
        destination.setLongitude(coordinates.getLongitude());
    }
}