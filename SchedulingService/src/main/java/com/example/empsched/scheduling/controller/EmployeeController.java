package com.example.empsched.scheduling.controller;

import com.example.empsched.shared.dto.scheduling.SchedulingEmployeeResponse;
import com.example.empsched.scheduling.mappers.DtoMapper;
import com.example.empsched.scheduling.mappers.RequestMapper;
import com.example.empsched.scheduling.service.EmployeeService;
import com.example.empsched.shared.dto.employee.CreateEmployeeRequest;
import com.example.empsched.shared.util.CredentialsExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final RequestMapper requestMapper;
    private final DtoMapper dtoMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<SchedulingEmployeeResponse> createEmployee(@RequestBody CreateEmployeeRequest createEmployeeRequestDTO) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        return ResponseEntity.ok(dtoMapper.toDto(employeeService.createEmployee(requestMapper.toEntity(createEmployeeRequestDTO), organisationId)));
    }

    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        employeeService.deleteEmployee(employeeId, organisationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<List<SchedulingEmployeeResponse>> getAllEmployees() {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        return ResponseEntity.ok(dtoMapper.toDtoList(employeeService.getAllEmployees(organisationId)));
    }

}
