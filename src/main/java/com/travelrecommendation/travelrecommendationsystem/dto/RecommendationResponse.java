package com.travelrecommendation.travelrecommendationsystem.dto;



import com.travelrecommendation.travelrecommendationsystem.model.ScoredDestination;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecommendationResponse {

    private List<ScoredDestination> recommendations;

    public RecommendationResponse(List<ScoredDestination> recommendations){
        this.recommendations = recommendations;
    }

}
