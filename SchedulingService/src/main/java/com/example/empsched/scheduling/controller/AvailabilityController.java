package com.example.empsched.scheduling.controller;

import com.example.empsched.scheduling.mappers.DtoMapper;
import com.example.empsched.shared.dto.scheduling.CreateAvailabilityRequest;
import com.example.empsched.scheduling.service.EmployeeAvailabilityService;
import com.example.empsched.shared.dto.scheduling.EmployeeAvailabilitiesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/availabilities")
@RequiredArgsConstructor
public class AvailabilityController {

    private final EmployeeAvailabilityService employeeAvailabilityService;
    private final DtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<EmployeeAvailabilitiesResponse> createAvailability(@RequestBody CreateAvailabilityRequest createAvailabilityDTO) {
        return ResponseEntity.ok(new EmployeeAvailabilitiesResponse(dtoMapper.toAvailabilityResponseList(employeeAvailabilityService.createEmployeeAvailabilities(createAvailabilityDTO))));
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeAvailabilitiesResponse> getAvailabilities(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(new EmployeeAvailabilitiesResponse(dtoMapper.toAvailabilityResponseList(employeeAvailabilityService.getEmployeeAvailability(employeeId))));
    }

    @DeleteMapping("/absences/{absenceId}")
    public ResponseEntity<Void> deleteAvailability(
            @PathVariable UUID absenceId
    ) {
        employeeAvailabilityService.deleteEmployeeUnavailability(absenceId);
        return ResponseEntity.noContent().build();
    }


}
