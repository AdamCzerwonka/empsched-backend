package com.example.empsched.employee.workflow;

import com.example.empsched.shared.dto.scheduling.CreateAvailabilityRequest;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ApproveAbsenceWorkflow {

    @WorkflowMethod
    void approveAbsence(final CreateAvailabilityRequest absenceRequest,final RequestContext context);
}
