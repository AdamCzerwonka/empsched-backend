package com.example.empsched.employee.workflow;

import com.example.empsched.shared.dto.position.EmployeePositionChangeRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.shared.util.RequestContext;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

import java.util.List;

@WorkflowInterface
public interface RemoveEmployeePositionWorkflow {
    @WorkflowMethod
    List<PositionResponse> remove(final EmployeePositionChangeRequest request, final RequestContext context);
}
