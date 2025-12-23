package com.hospital.hospital_visits.service;

import com.hospital.hospital_visits.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.Map;
/*
*
*  service class that allows
*  me to connect with the corresponding
*  repository of the entity and possibly
*  perform operations on the data transfer object (dto)
*
* */

@Service
public class WeatherService {

    private final WebClient webClient;

    public WeatherService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Map getDailyWeather(double lat, double lon, LocalDate date) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("api.open-meteo.com")
                            .path("/v1/forecast")
                            .queryParam("latitude", lat)
                            .queryParam("longitude", lon)
                            .queryParam("daily", "temperature_2m_max,temperature_2m_min")
                            .queryParam("timezone", "Europe/Rome")
                            .queryParam("start_date", date.toString())
                            .queryParam("end_date", date.toString())
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception ex) {
            throw new BadRequestException("Weather API failed: " + ex.getMessage());
        }
    }
}
