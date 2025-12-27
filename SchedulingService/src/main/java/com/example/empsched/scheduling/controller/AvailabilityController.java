package com.example.empsched.scheduling.controller;

import com.example.empsched.scheduling.dto.CreateAvailabilityDTO;
import com.example.empsched.scheduling.entity.EmployeeAvailability;
import com.example.empsched.scheduling.service.EmployeeAvailabilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/availabilities")
@RequiredArgsConstructor
public class AvailabilityController {

    private final EmployeeAvailabilityService employeeAvailabilityService;

    @PostMapping
    public ResponseEntity<List<EmployeeAvailability>> createAvailability(
            @RequestBody CreateAvailabilityDTO createAvailabilityDTO
    ) {
        return ResponseEntity.ok(employeeAvailabilityService.createEmployeeAvailabilities(createAvailabilityDTO));
    }


    @GetMapping("/{employeeId}")
    public ResponseEntity<List<EmployeeAvailability>> getAvailabilities(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(employeeAvailabilityService.getEmployeeAvailability(employeeId));
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteAvailability(
            @PathVariable UUID employeeId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam boolean isAvailable
    ) {
        employeeAvailabilityService.deleteEmployeeAvailability(startDate, endDate, employeeId, isAvailable);
        return ResponseEntity.noContent().build();
    }


}
