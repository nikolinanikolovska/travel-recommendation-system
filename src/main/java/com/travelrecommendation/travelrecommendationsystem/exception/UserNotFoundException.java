package com.travelrecommendation.travelrecommendationsystem.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User not found.");
    }
}