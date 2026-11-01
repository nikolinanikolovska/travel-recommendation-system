package com.travelrecommendation.travelrecommendationsystem.service;

import com.travelrecommendation.travelrecommendationsystem.model.Destination;

import java.util.List;

public interface DestinationService {

    List<Destination> findAll();
    Destination findById(Long id);
    Destination save(Destination destination);
    Destination update(Long id, Destination destination);
    void deleteById(Long id);
}
