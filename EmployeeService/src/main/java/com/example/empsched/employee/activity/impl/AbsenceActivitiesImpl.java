package com.example.empsched.employee.activity.impl;

import com.example.empsched.employee.activity.AbsenceActivities;
import com.example.empsched.employee.util.WorkflowTasks;
import com.example.empsched.shared.client.SchedulingServiceClient;
import com.example.empsched.shared.dto.scheduling.CreateAvailabilityRequest;
import com.example.empsched.shared.dto.scheduling.EmployeeAvailabilitiesResponse;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@ActivityImpl(workers = {WorkflowTasks.WORKER_ABSENCE_MANAGEMENT})
@RequiredArgsConstructor
public class AbsenceActivitiesImpl implements AbsenceActivities {

    private final SchedulingServiceClient schedulingServiceClient;

    @Override
    public EmployeeAvailabilitiesResponse createEmployeeAbsences(CreateAvailabilityRequest request, RequestContext requestContext) {
        return schedulingServiceClient.createEmployeeAvailabilities(request, requestContext).getBody();
    }

    @Override
    public void deleteEmployeeAbsencesByEmployeeId(final UUID absenceId,
                                                   final RequestContext context) {
        schedulingServiceClient.deleteEmployeeAvailabilities(
                absenceId,
                context
        );
    }
}
