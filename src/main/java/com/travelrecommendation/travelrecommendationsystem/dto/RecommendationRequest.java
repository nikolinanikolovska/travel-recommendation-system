package com.travelrecommendation.travelrecommendationsystem.dto;

import com.travelrecommendation.travelrecommendationsystem.model.enums.Season;
import com.travelrecommendation.travelrecommendationsystem.model.enums.TravelType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecommendationRequest {

    private Double budget;
    private Double maxDistance;
    private Integer duration;
    private TravelType travelType;
    private Season season;

    public RecommendationRequest(Double budget,
                                 Double maxDistance,
                                 Integer duration,
                                 TravelType travelType,
                                 Season season) {
        this.budget = budget;
        this.maxDistance = maxDistance;
        this.duration = duration;
        this.travelType = travelType;
        this.season = season;
    }
}