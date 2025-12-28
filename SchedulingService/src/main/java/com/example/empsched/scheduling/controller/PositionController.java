package com.example.empsched.scheduling.controller;

import com.example.empsched.scheduling.entity.Position;
import com.example.empsched.scheduling.mappers.DtoMapper;
import com.example.empsched.scheduling.mappers.RequestMapper;
import com.example.empsched.scheduling.service.PositionService;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.shared.util.CredentialsExtractor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/positions")
@Slf4j
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;
    private final RequestMapper requestMapper;
    private final DtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<PositionResponse> createPosition(@Valid @RequestBody CreatePositionRequest position) {
        UUID organisationId = UUID.fromString("7123f3ec-3517-4d3e-98e2-4e98a4cd9581"); // TODO: Get from auth context when security is implemented
        return ResponseEntity.ok(dtoMapper.toResponse(positionService.createPosition(requestMapper.toEntity(position), organisationId)));
    }

    @DeleteMapping("/{positionId}")
    public ResponseEntity<Void> deletePosition(@PathVariable UUID positionId) {
        UUID organisationId = UUID.fromString("7123f3ec-3517-4d3e-98e2-4e98a4cd9581"); // TODO: Get from auth context when security is implemented
        positionService.deletePosition(positionId, organisationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<List<PositionResponse>> getEmployeePositions(@PathVariable UUID employeeId) {
        UUID organisationId = UUID.fromString("7123f3ec-3517-4d3e-98e2-4e98a4cd9581"); // TODO: Get from auth context when security is implemented
        return ResponseEntity.ok(dtoMapper.toPositionResponseList(positionService.getEmployeePositions(
                organisationId,
                employeeId
        )));
    }

    @PostMapping("/{positionId}/employees/{employeeId}")
    public ResponseEntity<List<PositionResponse>> addPositionToEmployee(@PathVariable UUID employeeId, @PathVariable UUID positionId) {
        UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        List<Position> positions = positionService.addPositionToEmployee(organisationId, employeeId, positionId);
        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.toPositionResponseList(positions));
    }

    @DeleteMapping("/{positionId}/employees/{employeeId}")
    public ResponseEntity<List<PositionResponse>> removePositionFromEmployee(@PathVariable UUID employeeId, @PathVariable UUID positionId) {
        UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        List<Position> positions = positionService.removePositionFromEmployee(organisationId, employeeId, positionId);
        return ResponseEntity.status(HttpStatus.OK).body(dtoMapper.toPositionResponseList(positions));
    }



}
