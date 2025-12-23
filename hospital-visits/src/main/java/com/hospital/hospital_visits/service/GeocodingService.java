package com.hospital.hospital_visits.service;

import com.hospital.hospital_visits.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;


@Service
public class GeocodingService {

    private final WebClient webClient;

    public GeocodingService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Map<String, String> geocode(String address) {
        try {

            List<Map> results = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("nominatim.openstreetmap.org")
                            .path("/search")
                            .queryParam("format", "json")
                            .queryParam("q", address)
                            .queryParam("limit", 1)
                            .build())
                    .header("User-Agent", "hospital-visits-app")
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();

            if (results == null || results.isEmpty()) {
                throw new BadRequestException("Geocoding: address not found");
            }

            Map first = results.get(0);
            String lat = String.valueOf(first.get("lat"));
            String lon = String.valueOf(first.get("lon"));

            return Map.of("lat", lat, "lon", lon);

        } catch (Exception ex) {
            throw new BadRequestException("Geocoding failed: " + ex.getMessage());
        }
    }
}
