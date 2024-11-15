package com.doctorsapp.doctors.controller;


import com.doctorsapp.doctors.abha.m2.entity.CareContextDetails;
import com.doctorsapp.doctors.abha.m2.entity.GenerateTokenDetails;
import com.doctorsapp.doctors.service.ApiService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestController
@RequestMapping("/public")
public class ApiPublicEntryController {
    private static final Logger logger = LoggerFactory.getLogger(ApiPublicEntryController.class);

    @Autowired
    private ApiService apiService;

    private volatile String callbackResponse = null;


    @PostMapping("/generate-token")
    public String generateToken(@RequestBody GenerateTokenDetails generateTokenDetails,
                                @RequestHeader Map<String, String> headers) {
        try {
            callbackResponse = null;
//            String accessToken = apiService.getSession();
//            JSONObject object = new JSONObject(accessToken);
//           String accessToken = object.getString("accessToken");

            String statusCode = apiService.generateToken(headers, generateTokenDetails);

            if ("202".equals(statusCode)) {
                synchronized (this) {
                    try {
                        if (callbackResponse == null) {
                            this.wait(30000);  // wait for 30 seconds
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return "Error waiting for callback";
                    }
                }

                return callbackResponse != null ? callbackResponse : "No callback response received in time";
            }

            return "Status code: " + statusCode;
        } catch (Exception e) {
            logger.error("Error generating token", e);
            return e.toString();
        }
    }


    @PostMapping("/api/v3/hip/token/on-generate-token")
    public ResponseEntity<String> onGenerateTokenCallback(@RequestBody String callbackData) {
        logger.info("Callback received: {}", callbackData);
        callbackResponse = callbackData;
        synchronized (this) {
            this.notify();
        }
        return ResponseEntity.ok("Callback received successfully");
    }

    @PostMapping("/v3/link/carecontext")
    public String linkCareContext(@RequestBody CareContextDetails careContextDetails,
                                  @RequestHeader Map<String, String> headers) {
        try {
            return "";
        }catch (Exception e){
            e.printStackTrace();
            return e+"";
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("Service is healthy");
    }
}
