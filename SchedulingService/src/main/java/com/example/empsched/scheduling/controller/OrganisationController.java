package com.example.empsched.scheduling.controller;

import com.example.empsched.scheduling.entity.Employee;
import com.example.empsched.scheduling.entity.Organisation;
import com.example.empsched.scheduling.mappers.DtoMapper;
import com.example.empsched.scheduling.mappers.RequestMapper;
import com.example.empsched.scheduling.service.OrganisationService;
import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/organisations")
@RequiredArgsConstructor
public class OrganisationController {

    private final OrganisationService organisationService;
    private final RequestMapper requestMapper;
    private final DtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<OrganisationResponse> createOrganisation(@Valid @RequestBody CreateOrganisationRequest createOrganisationRequest) {
        Employee owner = new Employee(createOrganisationRequest.ownerId());
        Organisation organisation = organisationService.createOrganisationWithOwner(requestMapper.toEntity(createOrganisationRequest), owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toResponse(organisation));
    }


    @DeleteMapping("/{organisationId}")
    public ResponseEntity<Void> deleteOrganisation(@PathVariable UUID organisationId) {
        // TODO: Get organisationId from auth context when security is implemented and check if the user has permission to delete this organisation (?)
        organisationService.deleteOrganisation(organisationId);
        return ResponseEntity.noContent().build();
    }

}
