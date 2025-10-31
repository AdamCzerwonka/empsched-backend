package com.example.empsched.employee.controller;

import com.example.empsched.employee.entity.Position;
import com.example.empsched.employee.mapper.DtoMapper;
import com.example.empsched.employee.service.PositionService;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.shared.utils.CredentialsExtractor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class PositionController {
    private final PositionService positionService;
    private final DtoMapper mapper;

    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<PositionResponse> createPosition(@RequestBody @Valid final CreatePositionRequest request) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        final Position position = positionService.createPosition(mapper.mapToPosition(request), organisationId);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapToPositionResponse(position));
    }

    @GetMapping("/employees/{employeeId}")
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<List<PositionResponse>> getEmployeePositions(@PathVariable final UUID employeeId) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        final List<Position> positions = positionService.getEmployeePositions(organisationId, employeeId);
        return ResponseEntity.status(HttpStatus.OK).body(positions.stream().map(mapper::mapToPositionResponse).toList());
    }

    @PostMapping("/{positionId}/employees/{employeeId}")
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<List<PositionResponse>> addPositionToEmployee(@PathVariable final UUID employeeId, @PathVariable final UUID positionId) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        final List<Position> positions = positionService.addPositionToEmployee(organisationId, employeeId, positionId);
        return ResponseEntity.status(HttpStatus.OK).body(positions.stream().map(mapper::mapToPositionResponse).toList());
    }

    @DeleteMapping("/{positionId}/employees/{employeeId}")
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<List<PositionResponse>> removePositionFromEmployee(@PathVariable final UUID employeeId, @PathVariable final UUID positionId) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        final List<Position> positions = positionService.removePositionFromEmployee(organisationId, employeeId, positionId);
        return ResponseEntity.status(HttpStatus.OK).body(positions.stream().map(mapper::mapToPositionResponse).toList());
    }

    @DeleteMapping("/{positionId}")
    @PreAuthorize("hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<Void> deletePosition(@PathVariable final UUID positionId) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        positionService.deletePosition(organisationId, positionId);
        return ResponseEntity.noContent().build();
    }
}
