package com.travelrecommendation.travelrecommendationsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String city;

    public RegisterRequest(String username,
                           String email,
                           String password,
                           String city) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.city = city;
    }
}