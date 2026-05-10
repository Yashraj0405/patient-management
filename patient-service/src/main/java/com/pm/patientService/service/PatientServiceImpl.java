package com.pm.patientService.service;

import com.pm.patientService.dto.PatientRequestDTO;
import com.pm.patientService.dto.PatientResponseDTO;
import com.pm.patientService.exception.EmailAlreadyExistsException;
import com.pm.patientService.exception.PatientNotFound;
import com.pm.patientService.grpc.BillingServiceGrpcClient;
import com.pm.patientService.mapper.PatientMapper;
import com.pm.patientService.model.Patient;
import com.pm.patientService.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;

    private final BillingServiceGrpcClient billingServiceGrpcClient;

    public PatientServiceImpl(PatientRepository patientRepository,BillingServiceGrpcClient billingServiceGrpcClient) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
    }

    public List<PatientResponseDTO> getAllPatients() {
        //return patientRepository.findAll().stream().map(patient -> PatientMapper.toDto(patient)).toList();
        return patientRepository.findAll().stream().map(PatientMapper::toDto).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {

        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + patientRequestDTO.getEmail());
        }

        Patient patient =patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        billingServiceGrpcClient.createBillingAccount(patient.getId().toString(),patient.getName(),patient.getEmail());
        return PatientMapper.toDto(patient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFound("Patient not found with id: " + id));

        if (!existingPatient.getEmail().equals(patientRequestDTO.getEmail()) &&
                patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException("Email already exists: " + patientRequestDTO.getEmail());
        }

        existingPatient.setName(patientRequestDTO.getName());
        existingPatient.setEmail(patientRequestDTO.getEmail());
        existingPatient.setAddress(patientRequestDTO.getAddress());
        existingPatient.setDateOfBirth(existingPatient.getDateOfBirth());

        return PatientMapper.toDto(patientRepository.save(existingPatient));
    }

    @Override
    public String deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFound("Patient not found with id: " + id);
        }
        UUID deletedId = id;
        patientRepository.deleteById(id);
        return "Patient with id: " + deletedId + " has been deleted successfully.";
    }

}
