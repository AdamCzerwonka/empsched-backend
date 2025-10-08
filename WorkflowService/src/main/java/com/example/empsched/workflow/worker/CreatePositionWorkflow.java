package com.example.empsched.workflow.worker;

import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.workflow.util.RequestContext;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CreatePositionWorkflow {
    @WorkflowMethod
    PositionResponse create(final CreatePositionRequest request, final RequestContext context);
}
