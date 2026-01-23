package com.example.empsched.employee.activity.impl;

import com.example.empsched.employee.activity.AbsenceActivities;
import com.example.empsched.employee.entity.Absence;
import com.example.empsched.employee.service.AbsenceService;
import com.example.empsched.employee.util.WorkflowTasks;
import com.example.empsched.shared.client.SchedulingServiceClient;
import com.example.empsched.shared.dto.scheduling.CreateAvailabilityRequest;
import com.example.empsched.shared.dto.scheduling.EmployeeAvailabilitiesResponse;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ActivityImpl(workers = {WorkflowTasks.WORKER_ABSENCE_MANAGEMENT})
@RequiredArgsConstructor
public class AbsenceActivitiesImpl implements AbsenceActivities {

    private final SchedulingServiceClient schedulingServiceClient;
    private final AbsenceService absenceService;

    @Override
    public EmployeeAvailabilitiesResponse createEmployeeAbsencesInSchedulingService(final CreateAvailabilityRequest request, final RequestContext requestContext) {
        return schedulingServiceClient.createEmployeeAvailabilities(request, requestContext).getBody();
    }

    @Override
    public void deleteEmployeeAbsencesInSchedulingService(final UUID absenceId,
                                                          final RequestContext context) {
        schedulingServiceClient.deleteEmployeeAvailabilities(
                absenceId,
                context
        );
    }

    @Override
    public void approveAbsenceInEmployeeService(final UUID absenceId, final RequestContext requestContext) {
        absenceService.approveAbsence(absenceId, requestContext.getOrganisationId());
    }

    @Override
    public void unapproveAbsenceInEmployeeService(final UUID absenceId, final RequestContext requestContext) {
        absenceService.unapproveAbsence(absenceId, requestContext.getOrganisationId());
    }
}
