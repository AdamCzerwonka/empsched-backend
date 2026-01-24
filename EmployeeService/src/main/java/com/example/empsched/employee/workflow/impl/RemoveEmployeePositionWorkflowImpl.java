package com.example.empsched.employee.workflow.impl;

import com.example.empsched.employee.activity.EmployeePositionActivities;
import com.example.empsched.employee.util.WorkflowTasks;
import com.example.empsched.employee.workflow.RemoveEmployeePositionWorkflow;
import com.example.empsched.shared.dto.position.EmployeePositionChangeRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.shared.util.CustomActivityOptions;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
@WorkflowImpl(workers = WorkflowTasks.WORKER_EMPLOYEE_POSITION_MANAGEMENT)
@RequiredArgsConstructor
public class RemoveEmployeePositionWorkflowImpl implements RemoveEmployeePositionWorkflow {

    private final EmployeePositionActivities activities = Workflow.newActivityStub(
            EmployeePositionActivities.class,
            CustomActivityOptions.DEFAULT
    );

    @Override
    public List<PositionResponse> remove(final EmployeePositionChangeRequest request, final RequestContext context) {
        final Saga saga = new Saga(new Saga.Options.Builder()
                .setParallelCompensation(true)
                .build());

        final UUID employeeId = request.employeeId();
        final UUID positionId = request.positionId();

        try {
            saga.addCompensation(() -> activities.addPositionToEmployeeInEmployeeService(employeeId, positionId, context));
            final List<PositionResponse> employeePositions = activities.removePositionFromEmployeeInEmployeeService(employeeId, positionId, context);

            saga.addCompensation(() -> activities.addPositionToEmployeeInSchedulingService(employeeId, positionId, context));
            activities.removePositionFromEmployeeInSchedulingService(employeeId, positionId, context);

            return employeePositions;
        } catch (Exception e) {
            log.error("Error removing position {} from employee {}: {}", positionId, employeeId, e.getMessage());
            saga.compensate();
            throw e;
        }
    }
}
