package com.example.empsched.employee.activity;

import com.example.empsched.shared.dto.scheduling.CreateAvailabilityRequest;
import com.example.empsched.shared.dto.scheduling.EmployeeAvailabilitiesResponse;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.UUID;

@ActivityInterface
public interface AbsenceActivities {

    @ActivityMethod
    EmployeeAvailabilitiesResponse createEmployeeAbsences(final CreateAvailabilityRequest request, final RequestContext requestContext);

    @ActivityMethod
    void deleteEmployeeAbsencesByEmployeeId(final UUID absenceId,
                                            final RequestContext requestContext);
}
