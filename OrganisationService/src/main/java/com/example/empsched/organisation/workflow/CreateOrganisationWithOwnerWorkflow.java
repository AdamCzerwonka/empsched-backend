package com.example.empsched.organisation.workflow;

import com.example.empsched.organisation.entity.Organisation;
import com.example.empsched.shared.dto.organisation.CreateOrganisationWithOwnerRequest;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CreateOrganisationWithOwnerWorkflow {
    @WorkflowMethod
    Organisation create(final CreateOrganisationWithOwnerRequest request);
}
