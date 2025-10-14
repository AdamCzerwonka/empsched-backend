package com.example.empsched.employee.workflow;

import com.example.empsched.employee.entity.Employee;
import com.example.empsched.shared.dto.employee.CreateEmployeeRequest;
import com.example.empsched.shared.utils.RequestContext;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CreateEmployeeWorkflow {
    @WorkflowMethod
    Employee createEmployee(final CreateEmployeeRequest request, final RequestContext context);
}
