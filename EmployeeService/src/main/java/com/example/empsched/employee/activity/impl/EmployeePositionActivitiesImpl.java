package com.example.empsched.employee.activity.impl;

import com.example.empsched.employee.activity.EmployeePositionActivities;
import com.example.empsched.employee.entity.Position;
import com.example.empsched.employee.mapper.DtoMapper;
import com.example.empsched.employee.service.PositionService;
import com.example.empsched.employee.util.WorkflowTasks;
import com.example.empsched.shared.client.SchedulingServiceClient;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@ActivityImpl(workers = {WorkflowTasks.WORKER_EMPLOYEE_POSITION_MANAGEMENT})
@RequiredArgsConstructor
public class EmployeePositionActivitiesImpl implements EmployeePositionActivities {
    private final PositionService positionService;
    private final DtoMapper mapper;
    private final SchedulingServiceClient schedulingServiceClient;

    @Override
    public List<PositionResponse> addPositionToEmployeeInEmployeeService(final UUID employeeId, final UUID positionId, final RequestContext context) {
        final List<Position> positions = positionService.addPositionToEmployee(context.getOrganisationId(), employeeId, positionId);
        return positions.stream().map(mapper::mapToPositionResponse).toList();
    }

    @Override
    public List<PositionResponse> removePositionFromEmployeeInEmployeeService(final UUID employeeId, final UUID positionId, final RequestContext context) {
        final List<Position> positions = positionService.removePositionFromEmployee(context.getOrganisationId(), employeeId, positionId);
        return positions.stream().map(mapper::mapToPositionResponse).toList();
    }

    @Override
    public List<PositionResponse> addPositionToEmployeeInSchedulingService(final UUID employeeId, final UUID positionId, final RequestContext context) {
        return schedulingServiceClient.addPositionToEmployee(employeeId, positionId, context).getBody();
    }

    @Override
    public List<PositionResponse> removePositionFromEmployeeInSchedulingService(final UUID employeeId, final UUID positionId, final RequestContext context) {
        return schedulingServiceClient.removePositionFromEmployee(employeeId, positionId, context).getBody();
    }
}
