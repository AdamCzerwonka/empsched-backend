package com.example.empsched.organisation.controller;

import com.example.empsched.organisation.dto.CreateOrganisationRequest;
import com.example.empsched.organisation.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrganisationController {
    private final OrganisationService organisationService;

    @PostMapping
    public ResponseEntity<Void> createOrganisation(@RequestBody CreateOrganisationRequest createOrganisationRequest) {
        organisationService.createOrganisation(createOrganisationRequest.name(),
                createOrganisationRequest.maxEmployees());
        return ResponseEntity.ok().build();
    }
}
