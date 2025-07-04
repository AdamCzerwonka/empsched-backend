package com.example.empsched.organisation.service.impl;

import com.example.empsched.organisation.dto.CreateOrganisationRequest;
import com.example.empsched.organisation.entity.Organisation;
import com.example.empsched.organisation.exception.OrganisationAlreadyExistsException;
import com.example.empsched.organisation.repository.OrganisationRepository;
import com.example.empsched.organisation.service.OrganisationService;
import com.example.empsched.shared.entity.Role;
import com.example.empsched.shared.rabbit.RoutingKeys;
import com.example.empsched.shared.dto.OrganisationCreateEvent;
import com.example.empsched.shared.dto.UserCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganisationServiceImpl implements OrganisationService {
    private final OrganisationRepository organisationRepository;
    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange topicExchange;

    @Override
    public void createOrganisation(final CreateOrganisationRequest request) {

        organisationRepository.findByName(request.name())
                .ifPresent(a -> {
                    throw new OrganisationAlreadyExistsException(a.getName());
                });

        final UserCreateEvent userCreateEvent = UserCreateEvent.builder()
                .id(UUID.randomUUID())
                .email(request.email())
                .password(request.password())
                .role(Role.ORGANISATION_ADMIN)
                .build();

        rabbitTemplate.convertAndSend(topicExchange.getName(), RoutingKeys.USER_CREATE, userCreateEvent);

        final Organisation organisation = new Organisation(request.name(), request.maxEmployees(),
                userCreateEvent.id());

        final OrganisationCreateEvent organisationCreateEvent = OrganisationCreateEvent.builder()
                .id(organisation.getId())
                .name(organisation.getName())
                .maxEmployees(organisation.getMaxEmployees())
                .build();

        rabbitTemplate.convertAndSend(topicExchange.getName(), RoutingKeys.ORGANISATION_CREATE,
                organisationCreateEvent);

        organisationRepository.save(organisation);
    }
}
