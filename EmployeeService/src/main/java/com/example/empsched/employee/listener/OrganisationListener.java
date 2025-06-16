package com.example.empsched.employee.listener;

import com.example.empsched.employee.service.OrganisationService;
import com.example.empsched.shared.dto.OrganisationCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrganisationListener {
    private final OrganisationService organisationService;

    @RabbitListener(queues = "organisation.create.queue")
    public void handleOrganisationCreated(OrganisationCreateEvent organisationCreateEvent) {
        organisationService.createOrganisation(organisationCreateEvent);
    }
}
