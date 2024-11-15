package com.doctorsapp.doctors.abha.m2.entity;


import lombok.Data;

import java.util.List;

@Data
public class CareContextDetails {
    private long abhaNumber;
    private String abhaAddress;
    private List<Patient> patient;

    @Data
    public static class Patient {
        private String referenceNumber;
        private String display;
        private List<CareContext> careContexts;
        private String hiType;
        private int count;
    }

    @Data
    public static class CareContext {
        private String referenceNumber;
        private String display;
    }
}

