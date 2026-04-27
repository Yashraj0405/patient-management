package com.pm.patientService.service;


import com.pm.patientService.dto.PatientResponseDTO;

import java.util.List;

public interface PatientService {

    public List<PatientResponseDTO> getAllPatients();
}
