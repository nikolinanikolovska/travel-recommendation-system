package com.travelrecommendation.travelrecommendationsystem.service;

import com.travelrecommendation.travelrecommendationsystem.model.Favorite;

import java.util.List;

public interface FavoriteService {

    Favorite addFavorite(Long userId, Long destinationId);

    List<Favorite> findByUserId(Long userId);

    void removeFavorite(Long favoriteId, Long userId);

    boolean existsByUserIdAndDestinationId(Long userId, Long destinationId);
}