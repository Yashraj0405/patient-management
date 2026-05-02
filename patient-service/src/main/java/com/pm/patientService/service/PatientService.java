package com.pm.patientService.service;


import com.pm.patientService.dto.PatientRequestDTO;
import com.pm.patientService.dto.PatientResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PatientService {

    public List<PatientResponseDTO> getAllPatients();

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO);
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO);
    public  String deletePatient(UUID id);
}
