package com.example.empsched.organisation.service.impl;

import com.example.empsched.organisation.entity.Organisation;
import com.example.empsched.organisation.repository.OrganisationRepository;
import com.example.empsched.organisation.service.OrganisationService;
import com.example.empsched.shared.dto.OrganisationCreateEvent;
import com.example.empsched.shared.utils.SerializerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganisationServiceImpl implements OrganisationService {
    private final OrganisationRepository organisationRepository;
    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange topicExchange;

    @Override
    public void createOrganisation(String name, int maxEmployees) {
        Organisation organisation = new Organisation(name, maxEmployees);

        OrganisationCreateEvent organisationCreateEvent = OrganisationCreateEvent.builder()
                .id(organisation.getId())
                .name(organisation.getName())
                .maxEmployees(organisation.getMaxEmployees())
                .build();

        String message = SerializerUtils.serialize(organisationCreateEvent);

        rabbitTemplate.convertAndSend(topicExchange.getName(), "organisation.create", message);

        organisationRepository.save(organisation);
    }
}
