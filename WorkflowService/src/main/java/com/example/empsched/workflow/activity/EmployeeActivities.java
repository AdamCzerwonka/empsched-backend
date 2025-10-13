package com.example.empsched.workflow.activity;

import com.example.empsched.shared.dto.employee.CreateEmployeeRequest;
import com.example.empsched.shared.dto.employee.EmployeeResponse;
import com.example.empsched.workflow.util.RequestContext;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.UUID;

@ActivityInterface
public interface EmployeeActivities {

    @ActivityMethod
    EmployeeResponse createEmployeeInEmployeeService(final CreateEmployeeRequest request, final RequestContext context);

    @ActivityMethod
    void deleteEmployeeInEmployeeService(final UUID employeeId, final RequestContext context);
}
