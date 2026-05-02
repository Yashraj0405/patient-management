package com.pm.patientService.dto;

import com.pm.patientService.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PatientRequestDTO {

    @NotNull(message = "Name is required")
    @Size(max  = 30, message = "Name must be at most 30 characters")
    @Size(min = 3, message = "Name must be at least 3 characters")
    private String name;

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Address is required")
    @Size(max  = 100, message = "Address must be at most 100 characters")
    @Size(min = 5, message = "Address must be at least 5 characters")
    private String address;

    @NotNull(message = "Date of Birth cannot be null")
    private String dateOfBirth;

    @NotBlank(groups = CreatePatientValidationGroup.class, message = "Registered Date is required")
    private String registeredDate;
}
