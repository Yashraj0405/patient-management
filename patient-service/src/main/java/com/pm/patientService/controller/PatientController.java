package com.pm.patientService.controller;

import com.pm.patientService.dto.PatientRequestDTO;
import com.pm.patientService.dto.PatientResponseDTO;
import com.pm.patientService.dto.validators.CreatePatientValidationGroup;
import com.pm.patientService.model.Patient;
import com.pm.patientService.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.Builder;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient" ,description = "API to manage patients")
public class PatientController {

    private  final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get all patients", description = "Retrieve a list of all patients in the system.")
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @PostMapping("/add-patient")
    @Operation(summary = "Create a new patient", description = "Add a new patient to the system with the provided details.")
    public ResponseEntity<PatientResponseDTO> createPatient(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO  patient = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing patient", description = "Update the details of an existing patient identified by their unique ID.")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO updatedPatient = patientService.updatePatient(id, patientRequestDTO);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a patient", description = "Remove a patient from the system using their unique ID.")
    public ResponseEntity<String> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok("Patient with id " + id + " deleted successfully.");
    }
}
