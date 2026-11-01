package com.travelrecommendation.travelrecommendationsystem.exception;

public class FavoriteAlreadyExistsException extends RuntimeException {

    public FavoriteAlreadyExistsException() {
        super("Destination is already in favorites.");
    }
}