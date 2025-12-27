package com.example.empsched.scheduling.service;

import com.example.empsched.scheduling.dto.CreateAvailabilityDTO;
import com.example.empsched.scheduling.entity.AvailabilityType;
import com.example.empsched.scheduling.entity.EmployeeAvailability;
import com.example.empsched.scheduling.repository.EmployeeAvailabilityRepository;
import com.example.empsched.scheduling.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeAvailabilityService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeAvailabilityRepository employeeAvailabilityRepository;

    public List<EmployeeAvailability> getEmployeeAvailability(UUID employeeId) {
        return employeeAvailabilityRepository.findAllByEmployeeId(employeeId);
    }

    public List<EmployeeAvailability> createEmployeeAvailabilities(CreateAvailabilityDTO createAvailabilityDTO) {
        List<EmployeeAvailability> employeeAvailabilities = new ArrayList<>();
        for (LocalDate date = createAvailabilityDTO.startDate(); !date.isAfter(createAvailabilityDTO.endDate()); date = date.plusDays(1)) {
            EmployeeAvailability availability = new EmployeeAvailability();
            availability.setEmployee(employeeRepository.getReferenceById(createAvailabilityDTO.employeeId()));
            availability.setDate(date);
            employeeAvailabilities.add(availability);
        }
        employeeAvailabilityRepository.saveAll(employeeAvailabilities);
        return employeeAvailabilities;
    }

    public void deleteEmployeeAvailability(LocalDate startDate, LocalDate endDate, UUID employeeId, boolean isAvailable) {
        employeeAvailabilityRepository.deleteByEmployeeIdAndDateBetweenAndTypeIs(
                employeeId,
                startDate,
                endDate,
                isAvailable ? AvailabilityType.DESIRED : AvailabilityType.UNAVAILABLE
        );
    }


}
