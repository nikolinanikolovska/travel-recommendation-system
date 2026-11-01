package com.travelrecommendation.travelrecommendationsystem.service.impl;

import com.travelrecommendation.travelrecommendationsystem.exception.DestinationNotFoundException;
import com.travelrecommendation.travelrecommendationsystem.exception.FavoriteAlreadyExistsException;
import com.travelrecommendation.travelrecommendationsystem.exception.UserNotFoundException;
import com.travelrecommendation.travelrecommendationsystem.model.Destination;
import com.travelrecommendation.travelrecommendationsystem.model.Favorite;
import com.travelrecommendation.travelrecommendationsystem.model.User;
import com.travelrecommendation.travelrecommendationsystem.repository.DestinationRepository;
import com.travelrecommendation.travelrecommendationsystem.repository.FavoriteRepository;
import com.travelrecommendation.travelrecommendationsystem.repository.UserRepository;
import com.travelrecommendation.travelrecommendationsystem.service.FavoriteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;

    public FavoriteServiceImpl(
            FavoriteRepository favoriteRepository,
            UserRepository userRepository,
            DestinationRepository destinationRepository) {

        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.destinationRepository = destinationRepository;
    }

    @Override
    public Favorite addFavorite(Long userId, Long destinationId) {
        if (favoriteRepository.existsByUserIdAndDestinationId(
                userId, destinationId)) {

            throw new FavoriteAlreadyExistsException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(DestinationNotFoundException::new);

        Favorite favorite = new Favorite(user, destination);

        return favoriteRepository.save(favorite);
    }

    @Override
    public List<Favorite> findByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    @Override
    public void removeFavorite(Long favoriteId, Long userId) {
        favoriteRepository.findByIdAndUserId(favoriteId, userId)
                .ifPresent(favoriteRepository::delete);
    }

    @Override
    public boolean existsByUserIdAndDestinationId(
            Long userId,
            Long destinationId) {

        return favoriteRepository.existsByUserIdAndDestinationId(
                userId,
                destinationId
        );
    }
}