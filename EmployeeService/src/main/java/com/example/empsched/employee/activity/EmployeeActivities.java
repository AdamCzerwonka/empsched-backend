package com.example.empsched.employee.activity;

import com.example.empsched.employee.entity.Employee;
import com.example.empsched.shared.dto.employee.CreateEmployeeRequest;
import com.example.empsched.shared.dto.scheduling.SchedulingEmployeeResponse;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.UUID;

@ActivityInterface
public interface EmployeeActivities {

    @ActivityMethod
    Employee createEmployeeInEmployeeService(final Employee employee, final RequestContext context);

    @ActivityMethod
    void deleteEmployeeInEmployeeService(final UUID employeeId, final RequestContext context);

    @ActivityMethod
    SchedulingEmployeeResponse createEmployeeInSchedulingService(final CreateEmployeeRequest employee, final RequestContext context);

    @ActivityMethod
    void deleteEmployeeInSchedulingService(final UUID employeeId, final RequestContext context);
}
