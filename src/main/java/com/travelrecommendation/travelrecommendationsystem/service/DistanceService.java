package com.travelrecommendation.travelrecommendationsystem.service;

public interface DistanceService {

    double calculateDistance(
            double latitude1,
            double longitude1,
            double latitude2,
            double longitude2
    );
}
