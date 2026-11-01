package com.travelrecommendation.travelrecommendationsystem.model;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScoredDestination {

    private Destination destination;
    private int score;

    public ScoredDestination(Destination destination, int score) {
        this.destination = destination;
        this.score = score;
    }
}
