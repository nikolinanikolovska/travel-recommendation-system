package com.travelrecommendation.travelrecommendationsystem.service;

import com.travelrecommendation.travelrecommendationsystem.model.Coordinates;

public interface GeocodingService {

    Coordinates getCoordinates(String location);
}
