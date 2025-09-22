package com.example.empsched.workflow.worker;

import com.example.empsched.shared.dto.organisation.CreateOrganisationWithOwnerRequest;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CreateOrganisationWithOwnerWorkflow {
    @WorkflowMethod
    void create(final CreateOrganisationWithOwnerRequest request);
}
