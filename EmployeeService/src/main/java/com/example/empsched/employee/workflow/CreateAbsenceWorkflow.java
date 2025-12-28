package com.example.empsched.employee.workflow;

import com.example.empsched.employee.dto.absence.CreateAbsenceRequest;
import com.example.empsched.employee.entity.Absence;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CreateAbsenceWorkflow {

    @WorkflowMethod
    Absence createAbsence(CreateAbsenceRequest request, RequestContext context);
}
