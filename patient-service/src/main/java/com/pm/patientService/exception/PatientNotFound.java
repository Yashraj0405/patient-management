package com.pm.patientService.exception;

public class PatientNotFound extends RuntimeException {
    public PatientNotFound(String message) {
        super(message);
    }
}
