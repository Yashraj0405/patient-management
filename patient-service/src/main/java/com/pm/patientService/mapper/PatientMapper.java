package com.pm.patientService.mapper;

import com.pm.patientService.dto.PatientResponseDTO;
import com.pm.patientService.model.Patient;

public class PatientMapper {

    public static PatientResponseDTO toDto(Patient patient) {
        PatientResponseDTO dto = new PatientResponseDTO();
        dto.setId(patient.getId().toString());
        dto.setName(patient.getName());
        dto.setEmail(patient.getEmail());
        dto.setAddress(patient.getAddress());
        dto.setDateOfBirth(patient.getDateOfBirth().toString());
        return dto;
    }
}
