package com.travelrecommendation.travelrecommendationsystem.service.impl;

import com.travelrecommendation.travelrecommendationsystem.dto.RegisterRequest;
import com.travelrecommendation.travelrecommendationsystem.exception.EmailAlreadyExistsException;
import com.travelrecommendation.travelrecommendationsystem.exception.InvalidCredentialsException;
import com.travelrecommendation.travelrecommendationsystem.exception.UserNotFoundException;
import com.travelrecommendation.travelrecommendationsystem.exception.UsernameAlreadyExistsException;
import com.travelrecommendation.travelrecommendationsystem.model.Coordinates;
import com.travelrecommendation.travelrecommendationsystem.model.User;
import com.travelrecommendation.travelrecommendationsystem.repository.UserRepository;
import com.travelrecommendation.travelrecommendationsystem.service.GeocodingService;
import com.travelrecommendation.travelrecommendationsystem.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GeocodingService geocodingService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            GeocodingService geocodingService,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.geocodingService = geocodingService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        updateCoordinates(user);

        return userRepository.save(user);
    }

    @Override
    public User update(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setCity(user.getCity());

        updateCoordinates(existingUser);

        return userRepository.save(existingUser);
    }

    @Override
    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getCity()
        );

        return save(user);
    }

    @Override
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return user;
    }

    private void updateCoordinates(User user) {
        Coordinates coordinates = geocodingService.getCoordinates(user.getCity());

        user.setLatitude(coordinates.getLatitude());
        user.setLongitude(coordinates.getLongitude());
    }
}