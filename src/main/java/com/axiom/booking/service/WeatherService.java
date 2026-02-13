package com.axiom.booking.service;

import com.axiom.booking.model.entity.Flight;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class WeatherService {

    private final String[] possibleWeather = {"Clear", "Cloudy", "Rainy", "Storm", "Severe"};

    private final Random random = new Random();

    /**
     * Returns weather for a given flight
     * Currently simulated randomly
     */
    public String getWeatherForFlight(Flight flight) {
        if (flight.getWeatherCondition() != null && !flight.getWeatherCondition().isEmpty()) {
            return flight.getWeatherCondition();
        }

        // Random weather simulation
        String weather = possibleWeather[random.nextInt(possibleWeather.length)];
        flight.setWeatherCondition(weather); // optional: store for later
        return weather;
    }
}
