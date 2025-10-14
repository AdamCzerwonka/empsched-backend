package com.example.empsched.organisation.workflow;

import com.example.empsched.organisation.entity.Position;
import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.utils.RequestContext;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CreatePositionWorkflow {
    @WorkflowMethod
    Position create(final CreatePositionRequest request, final RequestContext context);
}
