package com.example.empsched.scheduling.controller;

import com.example.empsched.shared.dto.scheduling.SchedulingEmployeeResponse;
import com.example.empsched.scheduling.mappers.DtoMapper;
import com.example.empsched.scheduling.mappers.RequestMapper;
import com.example.empsched.scheduling.service.EmployeeService;
import com.example.empsched.shared.dto.employee.CreateEmployeeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<SchedulingEmployeeResponse> createEmployee(CreateEmployeeRequest createEmployeeRequestDTO) {
        UUID organisationId = UUID.fromString("7123f3ec-3517-4d3e-98e2-4e98a4cd9581"); // TODO: Get from auth context when security is implemented
        return ResponseEntity.ok(dtoMapper.toDto(employeeService.createEmployee(requestMapper.toEntity(createEmployeeRequestDTO), organisationId)));
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {
        UUID organisationId = UUID.fromString("7123f3ec-3517-4d3e-98e2-4e98a4cd9581"); // TODO: Get from auth context when security is implemented
        employeeService.deleteEmployee(employeeId, organisationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SchedulingEmployeeResponse>> getAllEmployees() {
        UUID organisationId = UUID.fromString("7123f3ec-3517-4d3e-98e2-4e98a4cd9581"); // TODO: Get from auth context when security is implemented
        return ResponseEntity.ok(dtoMapper.toDtoList(employeeService.getAllEmployees(organisationId)));
    }

}
