package com.example.empsched.employee.activity;

import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.activity.ActivityInterface;

import java.util.List;
import java.util.UUID;

@ActivityInterface
public interface EmployeePositionActivities {
    List<PositionResponse> addPositionToEmployeeInEmployeeService(final UUID employeeId, final UUID positionId, final RequestContext context);

    List<PositionResponse> removePositionFromEmployeeInEmployeeService(final UUID employeeId, final UUID positionId, final RequestContext context);

    List<PositionResponse> addPositionToEmployeeInSchedulingService(final UUID employeeId, final UUID positionId, final RequestContext context);

    List<PositionResponse> removePositionFromEmployeeInSchedulingService(final UUID employeeId, final UUID positionId, final RequestContext context);
}
