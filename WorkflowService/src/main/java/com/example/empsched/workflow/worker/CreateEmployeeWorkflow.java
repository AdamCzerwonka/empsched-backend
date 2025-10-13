package com.example.empsched.workflow.worker;

import com.example.empsched.shared.dto.employee.CreateEmployeeRequest;
import com.example.empsched.shared.dto.employee.EmployeeResponse;
import com.example.empsched.workflow.util.RequestContext;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CreateEmployeeWorkflow {
    @WorkflowMethod
    EmployeeResponse createEmployee(final CreateEmployeeRequest request, final RequestContext context);
}
