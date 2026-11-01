package com.travelrecommendation.travelrecommendationsystem.service;

import com.travelrecommendation.travelrecommendationsystem.dto.RegisterRequest;
import com.travelrecommendation.travelrecommendationsystem.model.User;

import java.util.List;

public interface UserService {

    User save(User user);

    User update(Long id, User user);

    User register(RegisterRequest request);

    User login(String username, String password);
}