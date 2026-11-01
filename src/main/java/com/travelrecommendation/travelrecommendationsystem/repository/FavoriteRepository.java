package com.travelrecommendation.travelrecommendationsystem.repository;

import com.travelrecommendation.travelrecommendationsystem.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByUserIdAndDestinationId(Long userId, Long destinationId);

    List<Favorite> findByUserId(Long userId);

    Optional<Favorite> findByIdAndUserId(Long favoriteId, Long userId);
}