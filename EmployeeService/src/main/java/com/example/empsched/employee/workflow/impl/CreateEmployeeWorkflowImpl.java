package com.example.empsched.employee.workflow.impl;

import com.example.empsched.employee.activity.EmployeeActivities;
import com.example.empsched.employee.activity.UserActivities;
import com.example.empsched.employee.entity.Employee;
import com.example.empsched.employee.mapper.DtoMapper;
import com.example.empsched.employee.mapper.RequestMapper;
import com.example.empsched.employee.util.WorkflowTasks;
import com.example.empsched.employee.workflow.CreateEmployeeWorkflow;
import com.example.empsched.shared.dto.employee.CreateEmployeeRequest;
import com.example.empsched.shared.dto.user.CreateUserRequest;
import com.example.empsched.shared.entity.Role;
import com.example.empsched.shared.util.CustomActivityOptions;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.failure.ActivityFailure;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@RequiredArgsConstructor
@WorkflowImpl(workers = WorkflowTasks.WORKER_EMPLOYEE_MANAGEMENT)
@Slf4j
public class CreateEmployeeWorkflowImpl implements CreateEmployeeWorkflow {
    private final RequestMapper mapper;
    private final DtoMapper dtoMapper;

    private final EmployeeActivities employeeActivities = Workflow.newActivityStub(
            EmployeeActivities.class,
            CustomActivityOptions.DEFAULT
    );

    private final UserActivities userActivities = Workflow.newActivityStub(
            UserActivities.class,
            CustomActivityOptions.DEFAULT
    );

    @Override
    public Employee createEmployee(CreateEmployeeRequest request, RequestContext context) {
        final Saga saga = new Saga(new Saga.Options.Builder()
                .setParallelCompensation(true)
                .build());

        final UUID employeeId = UUID.randomUUID();
        final CreateEmployeeRequest createEmployeeRequest = mapper.mapToCreateEmployeeRequest(employeeId, request);
        final CreateUserRequest createUserRequest = mapper.mapToCreateUserRequest(employeeId, context.getOrganisationId(), Role.ORGANISATION_EMPLOYEE, request);

        try {
            saga.addCompensation(() -> employeeActivities.deleteEmployeeInEmployeeService(employeeId, context));
            final Employee employee = employeeActivities.createEmployeeInEmployeeService(dtoMapper.mapToEmployee(createEmployeeRequest), context);

            saga.addCompensation(() -> userActivities.deleteUserInAuthService(employeeId));
            userActivities.createUserInAuthService(createUserRequest);

            return employee;
        } catch (ActivityFailure e) {
            log.error("Employee creation failed, starting compensation. Reason: {}", e.getMessage());
            saga.compensate();
            throw e;
        }
    }
}
