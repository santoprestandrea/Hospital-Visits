package com.hospital.hospital_visits.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.hospital_visits.entity.ClinicRoom;
import com.hospital.hospital_visits.entity.Visit;
import com.hospital.hospital_visits.exception.BadRequestException;
import com.hospital.hospital_visits.exception.ResourceNotFoundException;
import com.hospital.hospital_visits.repository.ClinicRoomRepository;
import com.hospital.hospital_visits.repository.VisitRepository;
import com.hospital.hospital_visits.service.GeocodingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
/*
*
* this api allows us to view external information, such as
* geolocation and weather, which information is associated with
* the address of the doctor's office and the visit, respectively.
* Through the repositories and the corresponding data transfer objects (dto)
*  we have allowed this. So we created a mapping of the paths and tried them
*  through the APIs
*
* */
@RestController
@RequestMapping("/api/external")
public class ExternalApiController {

    private final GeocodingService geocodingService;
    private final ClinicRoomRepository clinicRoomRepository;
    private final VisitRepository visitRepository;


    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public ExternalApiController(GeocodingService geocodingService,
                                 ClinicRoomRepository clinicRoomRepository,
                                 VisitRepository visitRepository,
                                 ObjectMapper objectMapper) {
        this.geocodingService = geocodingService;
        this.clinicRoomRepository = clinicRoomRepository;
        this.visitRepository = visitRepository;
        this.objectMapper = objectMapper;

        this.restClient = RestClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @GetMapping("/rooms/{roomId}/geocode")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    public Map<String, String> geocodeRoom(@PathVariable Long roomId) {
        ClinicRoom room = clinicRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("ClinicRoom not found with id " + roomId));

        return geocodingService.geocode(room.getAddress());
    }

    @GetMapping("/visits/{visitId}/weather")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    public Map<String, Object> weatherForVisit(@PathVariable Long visitId,
                                               @RequestParam Long roomId) {

        Visit v = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id " + visitId));

        ClinicRoom room = clinicRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("ClinicRoom not found with id " + roomId));

        if (v.getDate() == null) {
            throw new BadRequestException("Visit date is missing");
        }

        Map<String, String> geo = geocodingService.geocode(room.getAddress());

        String latStr = geo.get("lat");
        String lonStr = geo.get("lon");
        if (latStr == null || lonStr == null) {
            throw new BadRequestException("Geocoding did not return lat/lon for address: " + room.getAddress());
        }

        double lat;
        double lon;
        try {
            lat = Double.parseDouble(latStr);
            lon = Double.parseDouble(lonStr);
        } catch (NumberFormatException ex) {
            throw new BadRequestException("Invalid lat/lon from geocoding: lat=" + latStr + ", lon=" + lonStr);
        }

        LocalDate date = v.getDate();
        return callOpenMeteoDaily(lat, lon, date);
    }


    private Map<String, Object> callOpenMeteoDaily(double lat, double lon, LocalDate date) {

        // ATTENZIONE: Open-Meteo forecast può non coprire date troppo lontane.
        // In quel caso ritorniamo un 400 chiaro (non 500).
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.open-meteo.com/v1/forecast")
                .queryParam("latitude", lat)
                .queryParam("longitude", lon)
                .queryParam("timezone", "Europe/Rome")
                .queryParam("start_date", date.toString())
                .queryParam("end_date", date.toString())
                .queryParam("daily", "temperature_2m_max,temperature_2m_min,precipitation_sum,wind_speed_10m_max,weathercode")
                .build()
                .toUriString();

        try {
            String json = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(json);
            JsonNode daily = root.path("daily");
            JsonNode times = daily.path("time");

            if (!times.isArray() || times.size() == 0) {
                throw new BadRequestException("No daily weather available for date " + date + " (provider limit)");
            }

            int idx = 0;

            Map<String, Object> out = new HashMap<>();
            out.put("date", times.get(idx).asText());

            out.put("tempMax", daily.path("temperature_2m_max").isArray() ? daily.path("temperature_2m_max").get(idx).asDouble() : null);
            out.put("tempMin", daily.path("temperature_2m_min").isArray() ? daily.path("temperature_2m_min").get(idx).asDouble() : null);
            out.put("precipitationSum", daily.path("precipitation_sum").isArray() ? daily.path("precipitation_sum").get(idx).asDouble() : null);
            out.put("windSpeedMax", daily.path("wind_speed_10m_max").isArray() ? daily.path("wind_speed_10m_max").get(idx).asDouble() : null);
            out.put("weatherCode", daily.path("weathercode").isArray() ? daily.path("weathercode").get(idx).asInt() : null);

            out.put("latitude", root.path("latitude").asDouble(lat));
            out.put("longitude", root.path("longitude").asDouble(lon));
            out.put("timezone", root.path("timezone").asText("Europe/Rome"));

            return out;

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Weather API call failed: " + e.getMessage());
        }
    }
}
