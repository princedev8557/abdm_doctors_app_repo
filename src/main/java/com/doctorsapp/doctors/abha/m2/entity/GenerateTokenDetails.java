package com.doctorsapp.doctors.abha.m2.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
public class GenerateTokenDetails {
    private long abhaNumber;
    private String abhaAddress;
    private String name;
    private String gender;
    private int yearOfBirth;
}
