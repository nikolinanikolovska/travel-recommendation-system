package com.travelrecommendation.travelrecommendationsystem.service.impl;

import com.travelrecommendation.travelrecommendationsystem.dto.RecommendationRequest;
import com.travelrecommendation.travelrecommendationsystem.dto.RecommendationResponse;
import com.travelrecommendation.travelrecommendationsystem.model.Destination;
import com.travelrecommendation.travelrecommendationsystem.model.ScoredDestination;
import com.travelrecommendation.travelrecommendationsystem.model.User;
import com.travelrecommendation.travelrecommendationsystem.repository.DestinationRepository;
import com.travelrecommendation.travelrecommendationsystem.service.DistanceService;
import com.travelrecommendation.travelrecommendationsystem.service.RecommendationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationServiceImpl
        implements RecommendationService {

    private final DestinationRepository destinationRepository;
    private final DistanceService distanceService;

    public RecommendationServiceImpl(
            DestinationRepository destinationRepository,
            DistanceService distanceService) {

        this.destinationRepository =
                destinationRepository;

        this.distanceService =
                distanceService;
    }

    @Override
    public RecommendationResponse recommend(
            RecommendationRequest request,
            User user) {

        List<ScoredDestination> recommendations =
                destinationRepository.findAll()
                        .stream()

                        /*
                         * ------------------------------------------------
                         * BUDGET FILTER
                         * ------------------------------------------------
                         *
                         * Total trip cost =
                         *
                         * averageDailyCost * duration
                         *
                         * Destination must fit inside user's budget.
                         */
                        .filter(destination ->
                                request.getBudget() == null ||
                                        request.getDuration() == null ||
                                        destination.getAverageDailyCost()
                                                * request.getDuration()
                                                <= request.getBudget())

                        /*
                         * ------------------------------------------------
                         * TRAVEL TYPE FILTER
                         * ------------------------------------------------
                         *
                         * Travel type is a hard requirement.
                         */
                        .filter(destination ->
                                request.getTravelType() == null ||
                                        destination.getTravelType()
                                                == request.getTravelType())

                        /*
                         * ------------------------------------------------
                         * DISTANCE FILTER
                         * ------------------------------------------------
                         *
                         * Distance is calculated only when the user
                         * provides a maximum distance.
                         *
                         * User coordinates come from the city entered
                         * during registration.
                         */
                        .filter(destination -> {

                            if (request.getMaxDistance() == null) {
                                return true;
                            }

                            double distance =
                                    distanceService.calculateDistance(
                                            user.getLatitude(),
                                            user.getLongitude(),
                                            destination.getLatitude(),
                                            destination.getLongitude()
                                    );


                            return distance <=
                                    request.getMaxDistance();
                        })

                        /*
                         * ------------------------------------------------
                         * SCORE REMAINING DESTINATIONS
                         * ------------------------------------------------
                         */

                        .map(destination -> {

                            int score = 0;

                            /*
                             * Season match
                             */
                            if (request.getSeason() != null &&
                                    destination.getSeason()
                                            == request.getSeason()) {

                                score += 4;
                            }

                            /*
                             * Duration match
                             *
                             * Destination is considered suitable
                             * when recommended duration is within
                             * +/- 1 day of requested duration.
                             */
                            if (request.getDuration() != null &&
                                    Math.abs(
                                            destination
                                                    .getRecommendedDuration()
                                                    - request.getDuration()
                                    ) <= 1) {

                                score += 2;
                            }

                            return new ScoredDestination(
                                    destination,
                                    score
                            );
                        })

                        /*
                         * ------------------------------------------------
                         * REMOVE ZERO-SCORE DESTINATIONS
                         * ------------------------------------------------
                         *
                         * If season or duration is provided,
                         * at least one scoring criterion must match.
                         */
                        .filter(scoredDestination -> {

                            boolean hasScoringCriteria =
                                    request.getSeason() != null ||
                                            request.getDuration() != null;

                            return !hasScoringCriteria ||
                                    scoredDestination.getScore() > 0;
                        })

                        /*
                         * SORT BY SCORE
                         */

                        .sorted((a, b) ->
                                Integer.compare(
                                        b.getScore(),
                                        a.getScore()
                                ))

                        /*
                         * LIMIT RESULTS
                         */

                        .limit(10)

                        .toList();

        return new RecommendationResponse(
                recommendations
        );
    }
}
