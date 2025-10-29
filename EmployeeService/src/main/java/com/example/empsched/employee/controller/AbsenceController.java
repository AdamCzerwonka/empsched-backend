package com.example.empsched.employee.controller;

import com.example.empsched.employee.dto.absence.AbsenceResponse;
import com.example.empsched.employee.dto.absence.CreateAbsenceRequest;
import com.example.empsched.employee.entity.Absence;
import com.example.empsched.employee.mapper.DtoMapper;
import com.example.empsched.employee.service.AbsenceService;
import com.example.empsched.shared.utils.CredentialsExtractor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/absences")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class AbsenceController {
    private final AbsenceService absenceService;
    private final DtoMapper dtoMapper;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AbsenceResponse> createSelfAbsence(@RequestBody @Valid final CreateAbsenceRequest request) {
        final Absence createdAbsence = absenceService.createUnapprovedAbsence(
                dtoMapper.mapToAbsence(request),
                CredentialsExtractor.getUserIdFromContext()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.mapToAbsenceResponse(createdAbsence));
    }

    @PostMapping("/employees/{employeeId}")
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<AbsenceResponse> createEmployeeAbsence(@RequestBody @Valid final CreateAbsenceRequest request, @PathVariable final UUID employeeId) {
        final Absence createdAbsence = absenceService.createApprovedAbsence(
                dtoMapper.mapToAbsence(request),
                employeeId,
                CredentialsExtractor.getOrganisationIdFromContext()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.mapToAbsenceResponse(createdAbsence));
    }
}
