package com.example.empsched.scheduling.controller;

import com.example.empsched.scheduling.dto.ShiftResponse;
import com.example.empsched.scheduling.dto.ShiftUpdateDTO;
import com.example.empsched.scheduling.mappers.DtoMapper;
import com.example.empsched.scheduling.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/shifts")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class ShiftController {

    private final ShiftService shiftService;
    private final DtoMapper dtoMapper;

    @PutMapping("/{shiftId}")
    public ResponseEntity<ShiftResponse> updateShift(
            @PathVariable UUID shiftId,
            @RequestBody ShiftUpdateDTO request) {
        return ResponseEntity.ok(dtoMapper.toDto(shiftService.updateShift(shiftId, request)));
    }

    @PostMapping("/schedule/{scheduleId}")
    public ResponseEntity<ShiftResponse> addShift(
            @PathVariable UUID scheduleId,
            @RequestBody ShiftUpdateDTO request) {
        return ResponseEntity.ok(dtoMapper.toDto(shiftService.createManualShift(scheduleId, request)));
    }

    @DeleteMapping("/{shiftId}")
    public ResponseEntity<Void> deleteShift(@PathVariable UUID shiftId) {
        shiftService.deleteShift(shiftId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{shiftId}/unassign")
    public ResponseEntity<ShiftResponse> unassignShift(@PathVariable UUID shiftId) {
        return ResponseEntity.ok(dtoMapper.toDto(shiftService.unassignShift(shiftId)));
    }

}
