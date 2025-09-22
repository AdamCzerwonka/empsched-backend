package com.example.empsched.organisation.controller;

import com.example.empsched.organisation.entity.Organisation;
import com.example.empsched.organisation.mapper.DtoMapper;
import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.organisation.service.OrganisationService;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrganisationController {
    private final OrganisationService organisationService;
    private final DtoMapper mapper;

    @PostMapping
    public ResponseEntity<OrganisationResponse> createOrganisation(@RequestBody @Valid final CreateOrganisationRequest request) {
        final Organisation organisation = organisationService.createOrganisation(mapper.mapToOrganisation(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapToOrganisationResponse(organisation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganisation(@PathVariable final UUID id) {
        organisationService.deleteOrganisation(id);
        return ResponseEntity.noContent().build();
    }
}
