package com.example.empsched.employee.controller;

import com.example.empsched.employee.entity.Employee;
import com.example.empsched.employee.entity.Organisation;
import com.example.empsched.employee.mapper.DtoMapper;
import com.example.empsched.employee.service.OrganisationService;
import com.example.empsched.shared.dto.organisation.CreateOrganisationWithOwnerRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/organisations" )
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class OrganisationController {
    private final OrganisationService organisationService;
    private final DtoMapper mapper;

    @PostMapping
    public ResponseEntity<OrganisationResponse> createOrganisationWithOwner(
            @RequestBody @Valid final CreateOrganisationWithOwnerRequest request,
            @RequestParam final UUID organisationId,
            @RequestParam final UUID ownerId) {
        final Organisation organisation = mapper.mapToOrganisation(organisationId, request);
        final Employee owner = mapper.mapToEmployee(ownerId, request);
        final Organisation created = organisationService.createOrganisationWithOwner(organisation, owner);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapToOrganisationResponse(created));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganisation(@PathVariable final UUID id) {
        organisationService.deleteOrganisation(id);
        return ResponseEntity.noContent().build();
    }
}
