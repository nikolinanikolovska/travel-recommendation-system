package com.travelrecommendation.travelrecommendationsystem.exception;

public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException(String location) {
        super("Location not found: " + location);
    }
}