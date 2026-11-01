package com.travelrecommendation.travelrecommendationsystem.model;

import com.travelrecommendation.travelrecommendationsystem.model.enums.Season;
import com.travelrecommendation.travelrecommendationsystem.model.enums.TravelType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;
    private String imageUrl;

    @Column(length = 1000)
    private String description;

    private Double latitude;
    private Double longitude;

    private Double averageDailyCost;
    private Integer recommendedDuration;

    @Enumerated(EnumType.STRING)
    private TravelType travelType;

    @Enumerated(EnumType.STRING)
    private Season season;

    public Destination(String name,
                       String country,
                       String description,
                       Double latitude,
                       Double longitude,
                       Double averageDailyCost,
                       Integer recommendedDuration,
                       TravelType travelType,
                       Season season) {
        this.name = name;
        this.country = country;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.averageDailyCost = averageDailyCost;
        this.recommendedDuration = recommendedDuration;
        this.travelType = travelType;
        this.season = season;
    }
}
