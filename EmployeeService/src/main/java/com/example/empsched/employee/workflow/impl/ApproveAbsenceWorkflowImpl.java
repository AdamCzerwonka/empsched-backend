package com.example.empsched.employee.workflow.impl;

import com.example.empsched.employee.activity.AbsenceActivities;
import com.example.empsched.employee.dto.absence.CreateAbsenceRequest;
import com.example.empsched.employee.entity.Absence;
import com.example.empsched.employee.util.WorkflowTasks;
import com.example.empsched.employee.workflow.ApproveAbsenceWorkflow;
import com.example.empsched.shared.dto.scheduling.CreateAvailabilityRequest;
import com.example.empsched.shared.util.CustomActivityOptions;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@RequiredArgsConstructor
@WorkflowImpl(workers = WorkflowTasks.WORKER_ABSENCE_MANAGEMENT)
@Slf4j
public class ApproveAbsenceWorkflowImpl implements ApproveAbsenceWorkflow {

    private final AbsenceActivities absenceActivities = Workflow.newActivityStub(
            AbsenceActivities.class,
            CustomActivityOptions.DEFAULT
    );

    @Override
    public void approveAbsence(CreateAvailabilityRequest absenceRequest, RequestContext context) {
        final Saga saga = new Saga(new Saga.Options.Builder()
                .setParallelCompensation(true)
                .build());
        final UUID absenceId = absenceRequest.absenceId();
        try {
            saga.addCompensation(() -> absenceActivities.unapproveAbsenceInEmployeeService(absenceId, context));
            absenceActivities.approveAbsenceInEmployeeService(absenceId, context);
            saga.addCompensation(() -> absenceActivities.deleteEmployeeAbsencesInSchedulingService(absenceId, context));
            absenceActivities.createEmployeeAbsencesInSchedulingService(
                    absenceRequest,
                    context
            );
        } catch (Exception e) {
            log.error("Error approving absence with id {}: {}", absenceId, e.getMessage());
            saga.compensate();
            throw e;
        }

    }
}
