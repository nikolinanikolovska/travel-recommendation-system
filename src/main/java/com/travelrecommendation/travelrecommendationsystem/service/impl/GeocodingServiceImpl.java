package com.travelrecommendation.travelrecommendationsystem.service.impl;

import com.travelrecommendation.travelrecommendationsystem.dto.NominatimResponse;
import com.travelrecommendation.travelrecommendationsystem.exception.LocationNotFoundException;
import com.travelrecommendation.travelrecommendationsystem.model.Coordinates;
import com.travelrecommendation.travelrecommendationsystem.service.GeocodingService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GeocodingServiceImpl implements GeocodingService {

    private final RestClient restClient;

    public GeocodingServiceImpl() {
        this.restClient = RestClient.builder()
                .baseUrl("https://nominatim.openstreetmap.org")
                .build();
    }

    @Override
    public Coordinates getCoordinates(String location) {

        NominatimResponse[] response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", location)
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .build())
                .header("User-Agent", "TravelRecommendationSystem/1.0")
                .retrieve()
                .body(NominatimResponse[].class);

        if (response == null || response.length == 0) {
            throw new LocationNotFoundException(location);
        }

        NominatimResponse result = response[0];

        return new Coordinates(
                Double.parseDouble(result.getLat()),
                Double.parseDouble(result.getLon())
        );
    }
}