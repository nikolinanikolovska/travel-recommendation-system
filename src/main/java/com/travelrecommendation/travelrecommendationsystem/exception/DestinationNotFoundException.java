package com.travelrecommendation.travelrecommendationsystem.exception;

public class DestinationNotFoundException extends RuntimeException {

    public DestinationNotFoundException() {
        super("Destination not found.");
    }
}