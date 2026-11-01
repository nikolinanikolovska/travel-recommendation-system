package com.travelrecommendation.travelrecommendationsystem.web.controller;

import com.travelrecommendation.travelrecommendationsystem.dto.ChatMessage;
import com.travelrecommendation.travelrecommendationsystem.dto.ChatbotRequest;
import com.travelrecommendation.travelrecommendationsystem.dto.ChatbotResponse;
import com.travelrecommendation.travelrecommendationsystem.dto.ChatbotStateResponse;
import com.travelrecommendation.travelrecommendationsystem.model.Destination;
import com.travelrecommendation.travelrecommendationsystem.model.User;
import com.travelrecommendation.travelrecommendationsystem.service.ChatbotService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @GetMapping
    public String getChatbotPage(
            HttpSession session,
            Model model) {

        List<ChatMessage> chatMessages =
                (List<ChatMessage>)
                        session.getAttribute("chatMessages");

        if (chatMessages == null) {

            chatMessages = new ArrayList<>();

            chatMessages.add(
                    new ChatMessage(
                            "BOT",
                            "Hello! 👋 I'm your travel assistant. " +
                                    "Tell me what kind of trip you are looking for."
                    )
            );

            chatMessages.add(
                    new ChatMessage(
                            "BOT",
                            "For example: " +
                                    "\"I want a cheap summer vacation for 5 days.\""
                    )
            );

            session.setAttribute(
                    "chatMessages",
                    chatMessages
            );

            session.setAttribute(
                    "chatbotConversation",
                    new HashMap<String, Object>()
            );
        }

        model.addAttribute(
                "chatMessages",
                chatMessages
        );

        model.addAttribute(
                "recommendations",
                session.getAttribute(
                        "chatbotRecommendations"
                )
        );

        return "chatbot";
    }


    @PostMapping
    public String sendMessage(
            ChatbotRequest request,
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("user");

        if (user == null) {

            model.addAttribute(
                    "error",
                    "Please login to use the travel assistant."
            );

            return "login";
        }

        List<ChatMessage> chatMessages =
                (List<ChatMessage>)
                        session.getAttribute("chatMessages");

        if (chatMessages == null) {

            chatMessages = new ArrayList<>();
        }

        chatMessages.add(
                new ChatMessage(
                        "USER",
                        request.getMessage()
                )
        );

        Map<String, Object> conversation =
                (Map<String, Object>)
                        session.getAttribute("chatbotConversation");

        if (conversation == null) {

            conversation = new HashMap<>();

            session.setAttribute(
                    "chatbotConversation",
                    conversation
            );
        }

        ChatbotResponse response =
                chatbotService.chat(
                        request,
                        user,
                        conversation
                );

        chatMessages.add(
                new ChatMessage(
                        "BOT",
                        response.getMessage()
                )
        );

        session.setAttribute(
                "chatMessages",
                chatMessages
        );

        session.setAttribute(
                "chatbotConversation",
                conversation
        );

        session.setAttribute(
                "chatbotRecommendations",
                response.getRecommendations()
        );

        return "redirect:/chatbot";
    }


    @PostMapping("/api")
    @ResponseBody
    public ChatbotResponse sendMessageAsync(
            @RequestBody ChatbotRequest request,
            HttpSession session) {

        User user =
                (User) session.getAttribute("user");

        if (user == null) {

            return new ChatbotResponse(
                    "Please login to use the travel assistant."
            );
        }


        // =========================================
        // GET CHAT HISTORY
        // =========================================

        List<ChatMessage> chatMessages =
                (List<ChatMessage>)
                        session.getAttribute("chatMessages");

        if (chatMessages == null) {

            chatMessages = new ArrayList<>();

            chatMessages.add(
                    new ChatMessage(
                            "BOT",
                            "Hello! 👋 I'm your travel assistant. " +
                                    "Tell me what kind of trip you are looking for."
                    )
            );

            chatMessages.add(
                    new ChatMessage(
                            "BOT",
                            "For example: " +
                                    "\"I want a cheap summer vacation for 5 days.\""
                    )
            );
        }


        // =========================================
        // ADD USER MESSAGE
        // =========================================

        chatMessages.add(
                new ChatMessage(
                        "USER",
                        request.getMessage()
                )
        );


        // =========================================
        // GET CONVERSATION
        // =========================================

        Map<String, Object> conversation =
                (Map<String, Object>)
                        session.getAttribute("chatbotConversation");

        if (conversation == null) {

            conversation = new HashMap<>();
        }


        // =========================================
        // CALL CHATBOT SERVICE
        // =========================================

        ChatbotResponse response =
                chatbotService.chat(
                        request,
                        user,
                        conversation
                );


        // =========================================
        // ADD BOT MESSAGE
        // =========================================

        chatMessages.add(
                new ChatMessage(
                        "BOT",
                        response.getMessage()
                )
        );


        // =========================================
        // SAVE STATE IN SESSION
        // =========================================

        session.setAttribute(
                "chatMessages",
                chatMessages
        );

        session.setAttribute(
                "chatbotConversation",
                conversation
        );

        session.setAttribute(
                "chatbotRecommendations",
                response.getRecommendations()
        );


        return response;
    }


    @GetMapping("/api")
    @ResponseBody
    public ChatbotStateResponse getChatbotState(
            HttpSession session) {

        List<ChatMessage> chatMessages =
                (List<ChatMessage>)
                        session.getAttribute("chatMessages");

        if (chatMessages == null) {

            chatMessages = new ArrayList<>();

            chatMessages.add(
                    new ChatMessage(
                            "BOT",
                            "Hello! 👋 I'm your travel assistant. " +
                                    "Tell me what kind of trip you are looking for."
                    )
            );

            chatMessages.add(
                    new ChatMessage(
                            "BOT",
                            "For example: " +
                                    "\"I want a cheap summer vacation for 5 days.\""
                    )
            );

            session.setAttribute(
                    "chatMessages",
                    chatMessages
            );
        }


        Map<String, Object> conversation =
                (Map<String, Object>)
                        session.getAttribute("chatbotConversation");

        if (conversation == null) {

            conversation = new HashMap<>();

            session.setAttribute(
                    "chatbotConversation",
                    conversation
            );
        }


        return new ChatbotStateResponse(
                chatMessages,
                (List<Destination>)
                        session.getAttribute(
                                "chatbotRecommendations"
                        )
        );
    }
}