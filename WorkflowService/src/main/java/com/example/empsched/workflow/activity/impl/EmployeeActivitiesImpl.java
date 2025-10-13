package com.example.empsched.workflow.activity.impl;

import com.example.empsched.shared.dto.employee.CreateEmployeeRequest;
import com.example.empsched.shared.dto.employee.EmployeeResponse;
import com.example.empsched.workflow.activity.EmployeeActivities;
import com.example.empsched.workflow.client.EmployeeServiceClient;
import com.example.empsched.workflow.util.RequestContext;
import com.example.empsched.workflow.util.Tasks;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ActivityImpl(workers = {Tasks.WORKER_EMPLOYEE_MANAGEMENT})
@RequiredArgsConstructor
public class EmployeeActivitiesImpl implements EmployeeActivities {
    private final EmployeeServiceClient employeeServiceClient;

    @Override
    public EmployeeResponse createEmployeeInEmployeeService(final CreateEmployeeRequest request, final RequestContext context) {
        return employeeServiceClient.createEmployee(request, context).getBody();
    }

    @Override
    public void deleteEmployeeInEmployeeService(final UUID employeeId, final RequestContext context) {
        employeeServiceClient.deleteEmployee(employeeId, context);
    }
}
