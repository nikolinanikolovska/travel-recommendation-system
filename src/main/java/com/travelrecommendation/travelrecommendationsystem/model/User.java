package com.travelrecommendation.travelrecommendationsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    private String city;

    private Double latitude;

    private Double longitude;

    public User(String username, String email, String password, String city) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.city = city;
    }
}