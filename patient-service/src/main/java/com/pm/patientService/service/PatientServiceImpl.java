package com.pm.patientService.service;

import com.pm.patientService.dto.PatientResponseDTO;
import com.pm.patientService.mapper.PatientMapper;
import com.pm.patientService.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getAllPatients() {
        //return patientRepository.findAll().stream().map(patient -> PatientMapper.toDto(patient)).toList();
        return patientRepository.findAll().stream().map(PatientMapper::toDto).toList();
    }

}
