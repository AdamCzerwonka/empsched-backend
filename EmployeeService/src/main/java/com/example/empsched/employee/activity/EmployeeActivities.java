package com.example.empsched.employee.activity;

import com.example.empsched.employee.entity.Employee;
import com.example.empsched.shared.utils.RequestContext;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.UUID;

@ActivityInterface
public interface EmployeeActivities {

    @ActivityMethod
    Employee createEmployeeInEmployeeService(final Employee employee, final RequestContext context);

    @ActivityMethod
    void deleteEmployeeInEmployeeService(final UUID employeeId, final RequestContext context);
}
