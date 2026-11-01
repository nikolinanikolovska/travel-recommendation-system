
        package com.travelrecommendation.travelrecommendationsystem.service.impl;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.google.gson.Gson;
import com.travelrecommendation.travelrecommendationsystem.dto.ChatbotPreferences;
import com.travelrecommendation.travelrecommendationsystem.dto.ChatbotRequest;
import com.travelrecommendation.travelrecommendationsystem.dto.ChatbotResponse;
import com.travelrecommendation.travelrecommendationsystem.dto.RecommendationRequest;
import com.travelrecommendation.travelrecommendationsystem.model.Destination;
import com.travelrecommendation.travelrecommendationsystem.model.ScoredDestination;
import com.travelrecommendation.travelrecommendationsystem.model.User;
import com.travelrecommendation.travelrecommendationsystem.model.enums.Season;
import com.travelrecommendation.travelrecommendationsystem.model.enums.TravelType;
import com.travelrecommendation.travelrecommendationsystem.service.ChatbotService;
import com.travelrecommendation.travelrecommendationsystem.service.RecommendationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatbotServiceImpl implements ChatbotService {

    private final Client geminiClient;
    private final Gson gson;
    private final RecommendationService recommendationService;

    public ChatbotServiceImpl(
            RecommendationService recommendationService) {

        this.geminiClient = new Client();
        this.gson = new Gson();
        this.recommendationService = recommendationService;
    }

    @Override
    public ChatbotResponse chat(
            ChatbotRequest request,
            User user,
            Map<String, Object> conversation) {

        String userMessage =
                request.getMessage().trim();

        /*
         * If recommendations were already generated,
         * the next user message starts a new recommendation request.
         */
        if (Boolean.TRUE.equals(
                conversation.get("recommendationGenerated"))) {

            conversation.clear();
        }

        /*
         * Handle simple duration answers such as:
         *
         * 5
         */
        if (userMessage.matches("\\d+")) {

            int duration =
                    Integer.parseInt(userMessage);

            conversation.put(
                    "duration",
                    duration
            );

            return continueConversation(
                    conversation,
                    user
            );
        }

        /*
         * Handle duration answers such as:
         *
         * 5 days
         * 5 day
         */
        if (userMessage.matches(
                "(?i)\\d+\\s*(day|days)"
        )) {

            String number =
                    userMessage.replaceAll(
                            "[^0-9]",
                            ""
                    );

            int duration =
                    Integer.parseInt(number);

            conversation.put(
                    "duration",
                    duration
            );

            return continueConversation(
                    conversation,
                    user
            );
        }

        /*
         * Prepare previously collected preferences.
         */
        String previousPreferences =
                gson.toJson(conversation);

        /*
         * Prompt used for extracting structured
         * travel preferences from the user's message.
         */
        String prompt = """
                You are a travel preference extraction assistant.

                The user is having a conversation with you.

                Your task is to extract travel preferences from
                the user's latest message and combine them with
                information already collected from previous messages.

                Valid travel types are:

                BEACH
                CULTURAL
                ADVENTURE
                CITY_BREAK
                NATURE
                RELAXATION

                Valid seasons are:

                SPRING
                SUMMER
                AUTUMN
                WINTER

                Extract these fields:

                - budget: total budget in euros, or null
                - maxDistance: maximum distance in kilometers, or null
                - duration: number of travel days, or null
                - travelType: one of the valid travel types, or null
                - season: one of the valid seasons, or null

                Previous collected preferences:
                %s

                Latest user message:
                %s

                Important rules:

                1. Keep previously collected information.
                2. Update a field only if the latest user message
                   provides a new value for that field.
                3. Do not invent values.
                4. If the user says "5 days", return duration = 5.
                5. If the user says "500 euros", return budget = 500.
                6. If the user says "within 300 km", return maxDistance = 300.
                7. "August", "July", "June" and similar summer months
                   should be interpreted as SUMMER.
                8. Return ONLY valid JSON.
                9. Do not recommend destinations.
                10. Do not add explanations.
                11. Do not use markdown.

                Return exactly this structure:

                {
                  "budget": null,
                  "maxDistance": null,
                  "duration": null,
                  "travelType": null,
                  "season": null
                }
                """.formatted(
                previousPreferences,
                userMessage
        );

        /*
         * Ask Gemini to extract the preferences.
         */
        GenerateContentResponse response;

        try {

            response =
                    geminiClient.models.generateContent(
                            "gemini-3.6-flash",
                            prompt,
                            null
                    );

        } catch (Exception e) {
            e.printStackTrace();

            return new ChatbotResponse(
                    "Gemini error: " + e.getMessage()
            );
        }



        String json =
                response.text();

        ChatbotPreferences preferences;

        try {

            preferences =
                    gson.fromJson(
                            json,
                            ChatbotPreferences.class
                    );

        } catch (Exception e) {

            /*
             * Gemini should return JSON, but if the
             * response cannot be converted into our DTO,
             * return a normal chatbot message.
             */
            return new ChatbotResponse(
                    "Sorry, I couldn't understand your travel preferences. " +
                            "Please try describing your trip again."
            );
        }

        /*
         * Store only non-null preferences.
         */
        if (preferences.getBudget() != null) {

            conversation.put(
                    "budget",
                    preferences.getBudget()
            );
        }

        if (preferences.getMaxDistance() != null) {

            conversation.put(
                    "maxDistance",
                    preferences.getMaxDistance()
            );
        }

        if (preferences.getDuration() != null) {

            conversation.put(
                    "duration",
                    preferences.getDuration()
            );
        }

        if (preferences.getTravelType() != null) {

            conversation.put(
                    "travelType",
                    preferences.getTravelType().name()
            );
        }

        if (preferences.getSeason() != null) {

            conversation.put(
                    "season",
                    preferences.getSeason().name()
            );
        }

        return continueConversation(
                conversation,
                user
        );
    }

    private ChatbotResponse continueConversation(
            Map<String, Object> conversation,
            User user) {

        /*
         * Duration is required before generating recommendations.
         */
        boolean hasDuration =
                conversation.containsKey("duration");

        /*
         * At least one additional preference is required.
         */
        boolean hasAdditionalPreference =
                conversation.containsKey("budget") ||
                        conversation.containsKey("maxDistance") ||
                        conversation.containsKey("travelType") ||
                        conversation.containsKey("season");

        /*
         * Ask for duration if it is missing.
         */
        if (!hasDuration) {

            return new ChatbotResponse(
                    "How many days would you like to travel?"
            );
        }

        /*
         * Ask for another travel preference if
         * no additional preference was provided.
         */
        if (!hasAdditionalPreference) {

            return new ChatbotResponse(
                    "What type of trip would you prefer? " +
                            "For example, beach, cultural, adventure, " +
                            "city break, nature or relaxation."
            );
        }

        /*
         * Convert stored conversation values
         * into the correct Java types.
         */
        Double budget =
                conversation.containsKey("budget")
                        ? ((Number)
                        conversation.get("budget"))
                        .doubleValue()
                        : null;

        Double maxDistance =
                conversation.containsKey("maxDistance")
                        ? ((Number)
                        conversation.get("maxDistance"))
                        .doubleValue()
                        : null;

        Integer duration =
                conversation.containsKey("duration")
                        ? ((Number)
                        conversation.get("duration"))
                        .intValue()
                        : null;

        TravelType travelType =
                conversation.containsKey("travelType")
                        ? TravelType.valueOf(
                        conversation
                                .get("travelType")
                                .toString()
                )
                        : null;

        Season season =
                conversation.containsKey("season")
                        ? Season.valueOf(
                        conversation
                                .get("season")
                                .toString()
                )
                        : null;

        /*
         * Build the recommendation request.
         */
        RecommendationRequest recommendationRequest =
                new RecommendationRequest(
                        budget,
                        maxDistance,
                        duration,
                        travelType,
                        season
                );

        /*
         * Use the existing recommendation system.
         *
         * Gemini does not generate destinations.
         * RecommendationService selects real destinations
         * from the database.
         */
        var recommendationResponse =
                recommendationService.recommend(
                        recommendationRequest,
                        user
                );

        List<ScoredDestination> scoredDestinations =
                recommendationResponse.getRecommendations();

        List<Destination> destinations =
                scoredDestinations.stream()
                        .map(ScoredDestination::getDestination)
                        .toList();

        /*
         * No matching destinations.
         */
        if (destinations.isEmpty()) {

            conversation.put(
                    "recommendationGenerated",
                    true
            );

            return new ChatbotResponse(
                    "Unfortunately, I couldn't find any " +
                            "destinations matching your preferences.",
                    destinations
            );
        }

        /*
         * Matching destinations were found.
         */
        String message =
                "Based on your preferences, I found "
                        + destinations.size()
                        + " suitable destinations for you.";

        /*
         * Mark the current recommendation request
         * as completed. The next user message will
         * start a new recommendation request.
         */
        conversation.put(
                "recommendationGenerated",
                true
        );

        return new ChatbotResponse(
                message,
                destinations
        );
    }
}
