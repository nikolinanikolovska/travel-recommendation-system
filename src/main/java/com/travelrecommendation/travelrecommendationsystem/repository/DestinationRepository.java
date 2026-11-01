package com.travelrecommendation.travelrecommendationsystem.repository;

import com.travelrecommendation.travelrecommendationsystem.model.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
}
