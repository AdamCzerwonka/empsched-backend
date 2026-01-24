package com.example.empsched.scheduling.controller;

import com.example.empsched.scheduling.entity.Employee;
import com.example.empsched.scheduling.entity.Organisation;
import com.example.empsched.scheduling.mappers.DtoMapper;
import com.example.empsched.scheduling.mappers.RequestMapper;
import com.example.empsched.scheduling.service.OrganisationService;
import com.example.empsched.shared.dto.organisation.CreateOrganisationRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import com.example.empsched.shared.util.CredentialsExtractor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/organisations")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class OrganisationController {

    private final OrganisationService organisationService;
    private final RequestMapper requestMapper;
    private final DtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<OrganisationResponse> createOrganisation(@Valid @RequestBody final CreateOrganisationRequest createOrganisationRequest) {
        Employee owner = new Employee(createOrganisationRequest.ownerId());
        Organisation organisation = organisationService.createOrganisationWithOwner(requestMapper.toEntity(createOrganisationRequest), owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.toResponse(organisation));
    }


    @DeleteMapping("/{organisationId}")
    public ResponseEntity<Void> deleteOrganisation(@PathVariable final UUID organisationId) {
        organisationService.deleteOrganisation(organisationId, CredentialsExtractor.getOrganisationIdFromContext());
        return ResponseEntity.noContent().build();
    }

}
