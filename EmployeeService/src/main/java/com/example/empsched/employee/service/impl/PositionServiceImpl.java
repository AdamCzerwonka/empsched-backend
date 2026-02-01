package com.example.empsched.employee.service.impl;

import com.example.empsched.employee.entity.Employee;
import com.example.empsched.employee.entity.Organisation;
import com.example.empsched.employee.entity.Position;
import com.example.empsched.employee.exception.EmployeeNotFoundException;
import com.example.empsched.employee.exception.OrganisationNotFoundException;
import com.example.empsched.employee.exception.PositionNotFoundException;
import com.example.empsched.employee.repository.EmployeeRepository;
import com.example.empsched.employee.repository.OrganisationRepository;
import com.example.empsched.employee.repository.PositionRepository;
import com.example.empsched.employee.service.PositionService;
import com.example.empsched.shared.client.AuthServiceClient;
import com.example.empsched.shared.dto.push.PushNotificationRequest;
import com.example.empsched.shared.util.BaseThrowChecks;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class PositionServiceImpl implements PositionService {
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final OrganisationRepository organisationRepository;
    private final AuthServiceClient authServiceClient;

    @Override
    public Position createPosition(final Position position, final UUID organisationId) {
        final Organisation organisation = organisationRepository.findById(organisationId).orElseThrow(() -> new OrganisationNotFoundException(organisationId));
        position.setOrganisation(organisation);
        return positionRepository.save(position);
    }

    @Override
    public List<Position> getEmployeePositions(final UUID callerOrganisationId, final UUID employeeId) {
        final Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, employee.getOrganisation().getId());

        return employee.getPositions().stream().toList();
    }

    @Override
    public List<Position> addPositionToEmployee(final UUID callerOrganisationId, final UUID employeeId, final UUID positionId) {
        log.info("Adding position {} to employee {}", positionId, employeeId);
        final Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, employee.getOrganisation().getId());

        final Position position = positionRepository.findById(positionId).orElseThrow(() -> new PositionNotFoundException(positionId));
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, position.getOrganisation().getId());

        employee.getPositions().add(position);
        List<Position> result = employeeRepository.save(employee).getPositions().stream().toList();
        log.info("Position added, sending push notification to {}", employee.getEmail());

        // Send push notification
        notifyPositionChange(employee, position, "POSITION_ASSIGNED");

        return result;
    }

    @Override
    public List<Position> removePositionFromEmployee(UUID callerOrganisationId, UUID employeeId, UUID positionId) {
        final Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, employee.getOrganisation().getId());

        final Position position = positionRepository.findById(positionId).orElseThrow(() -> new PositionNotFoundException(positionId));
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, position.getOrganisation().getId());

        employee.getPositions().remove(position);
        List<Position> result = employeeRepository.save(employee).getPositions().stream().toList();

        // Send push notification
        notifyPositionChange(employee, position, "POSITION_REMOVED");

        return result;
    }

    private void notifyPositionChange(Employee employee, Position position, String type) {
        log.info("Notifying position change: type={}, employee={}", type, employee.getEmail());
        try {
            String titleKey = type.equals("POSITION_ASSIGNED")
                    ? "notifications.position_assigned.title"
                    : "notifications.position_removed.title";
            String bodyKey = type.equals("POSITION_ASSIGNED")
                    ? "notifications.position_assigned.body"
                    : "notifications.position_removed.body";

            PushNotificationRequest request = new PushNotificationRequest(
                    employee.getEmail(),
                    type,
                    titleKey,
                    bodyKey,
                    Map.of("positionId", position.getId().toString()),
                    "/my-account-settings",
                    Map.of("positionId", position.getId().toString())
            );

            log.info("Sending push notification request to AuthService");
            authServiceClient.sendPushNotification(request);
            log.info("Push notification request sent successfully");
        } catch (Exception e) {
            log.warn("Failed to send push notification for position change: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deletePosition(final UUID callerOrganisationId, final UUID positionId) {
        Optional<Position> position = positionRepository.findById(positionId);
        if (position.isEmpty()) return;
        // TODO add checks for linked employees (?)
        BaseThrowChecks.throwIfNotRelated(callerOrganisationId, position.get().getOrganisation().getId());
        positionRepository.deleteById(positionId);
    }
}
