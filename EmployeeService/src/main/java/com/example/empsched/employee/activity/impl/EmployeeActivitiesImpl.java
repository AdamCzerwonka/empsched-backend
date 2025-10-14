package com.example.empsched.employee.activity.impl;

import com.example.empsched.employee.activity.EmployeeActivities;
import com.example.empsched.employee.entity.Employee;
import com.example.empsched.employee.service.EmployeeService;
import com.example.empsched.employee.util.WorkflowTasks;
import com.example.empsched.shared.utils.RequestContext;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ActivityImpl(workers = {WorkflowTasks.WORKER_EMPLOYEE_MANAGEMENT})
@RequiredArgsConstructor
public class EmployeeActivitiesImpl implements EmployeeActivities {
    private final EmployeeService employeeService;

    @Override
    public Employee createEmployeeInEmployeeService(final Employee employee, final RequestContext context) {
        return employeeService.createEmployee(employee, context.getOrganisationId());
    }

    @Override
    public void deleteEmployeeInEmployeeService(final UUID employeeId, final RequestContext context) {
        employeeService.deleteEmployee(employeeId, context.getOrganisationId());
    }
}
