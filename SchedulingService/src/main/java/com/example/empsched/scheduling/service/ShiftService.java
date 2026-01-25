package com.example.empsched.scheduling.service;


import com.example.empsched.scheduling.dto.ShiftUpdateDTO;
import com.example.empsched.scheduling.entity.Employee;
import com.example.empsched.scheduling.entity.Schedule;
import com.example.empsched.scheduling.entity.Shift;
import com.example.empsched.scheduling.exceptions.EmployeeNotFoundException;
import com.example.empsched.scheduling.exceptions.ScheduleNotFound;
import com.example.empsched.scheduling.exceptions.ShiftNotFoundExceptiom;
import com.example.empsched.scheduling.repository.EmployeeRepository;
import com.example.empsched.scheduling.repository.ScheduleRepository;
import com.example.empsched.scheduling.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;
    private final ScheduleRepository scheduleRepository;

    public Shift updateShift(final UUID shiftId, final ShiftUpdateDTO request) {
        final Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundExceptiom(shiftId));

        if (request.startTime() != null) {
            shift.setStartTime(request.startTime());
        }
        if (request.endTime() != null) {
            shift.setEndTime(request.endTime());
        }
        if (request.requiredPositionId() != null) {
            shift.setRequiredPositionId(request.requiredPositionId());
        }
        if (request.assignedEmployeeId() != null) {
            Employee employee = employeeRepository.findById(request.assignedEmployeeId())
                    .orElseThrow(() -> new EmployeeNotFoundException(request.assignedEmployeeId()));
            shift.setAssignedEmployee(employee);
        } else {
             shift.setAssignedEmployee(null);
        }
        return shiftRepository.save(shift);
    }

    public Shift createManualShift(UUID scheduleId, ShiftUpdateDTO request) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFound(scheduleId));

        Shift shift = new Shift();
        shift.setSchedule(schedule);
        shift.setStartTime(request.startTime());
        shift.setEndTime(request.endTime());
        if (request.requiredPositionId() != null) {
            shift.setRequiredPositionId(request.requiredPositionId());
        }
        if (request.assignedEmployeeId() != null) {
            Employee employee = employeeRepository.findById(request.assignedEmployeeId())
                    .orElseThrow(() -> new EmployeeNotFoundException(request.assignedEmployeeId()));
            shift.setAssignedEmployee(employee);
        }

        return shiftRepository.save(shift);
    }

    public void deleteShift(UUID shiftId) {
        if (!shiftRepository.existsById(shiftId)) {
            throw new ShiftNotFoundExceptiom(shiftId);
        }
        shiftRepository.deleteById(shiftId);
    }

    public Shift unassignShift(UUID shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundExceptiom(shiftId));
        shift.setAssignedEmployee(null);
        return shiftRepository.save(shift);
    }
}
