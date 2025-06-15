package com.example.empsched.employee.listener;

import com.example.empsched.employee.service.OrganisationService;
import com.example.empsched.shared.dto.OrganisationCreateEvent;
import com.example.empsched.shared.utils.SerializerUtils;
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
    public void handleOrganisationCreated(String message) {
        OrganisationCreateEvent organisationCreateEvent =
                SerializerUtils.deserialize(message, OrganisationCreateEvent.class);

        organisationService.createOrganisation(organisationCreateEvent);
    }
}
