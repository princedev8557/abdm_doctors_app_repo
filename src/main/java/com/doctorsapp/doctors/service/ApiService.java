package com.doctorsapp.doctors.service;


import com.doctorsapp.doctors.abha.m2.entity.GenerateTokenDetails;
import com.doctorsapp.doctors.constants.ApiUrl;
import com.doctorsapp.doctors.constants.AppConstants;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
public class ApiService {
    public String generateToken(Map<String, String> request_headers, GenerateTokenDetails generateTokenDetails) {
        RestTemplate restTemplate = new RestTemplate();

        //timestamp
        Instant now = Instant.now();
        Instant truncatedNow = Instant.ofEpochMilli(now.toEpochMilli());
        String iso_time = truncatedNow.toString();

        String access_token = request_headers.get("authorization").trim().replace("Bearer ", "");
        String timestamp = request_headers.get("timestamp").trim();
        String request_id = request_headers.get("request-id").trim();
        String x_cm_id = request_headers.get("x-cm-id").trim();
        String x_hip_id = request_headers.get("x-hip-id").trim();

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "*/*");
        headers.set("Accept-Language", "en-US");
        headers.set("Content-Type", "application/json");
        headers.set("REQUEST-ID", request_id);
        headers.set("TIMESTAMP", iso_time);
        headers.set("X-HIP-ID", x_hip_id);
        headers.set("X-CM-ID", x_cm_id);
        headers.set("Authorization", "Bearer " + access_token);

        HttpEntity<GenerateTokenDetails> entity = new HttpEntity<>(generateTokenDetails, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(ApiUrl.GENERATE_TOKEN, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.ACCEPTED) {
                return response.getStatusCodeValue() + "";  // Return the response body
            } else {
                return response.getStatusCode() + " " + response.getStatusCodeValue();
            }
        } catch (HttpClientErrorException e) {

            return e.getMessage();
        }

    }

    public String getSession() {
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Accept-Language", "en-US");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare the request body
        String body = "{\n" +
                "    \"clientId\" : \"" + AppConstants.CLIENT_ID + "\",\n" +
                "    \"clientSecret\" : \"" + AppConstants.CLIENT_SECRET + "\"\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // Create RestTemplate and make the request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(ApiUrl.SESSIONS, HttpMethod.POST, entity, String.class);

        // Return response body
        return response.getBody();
    }
}

