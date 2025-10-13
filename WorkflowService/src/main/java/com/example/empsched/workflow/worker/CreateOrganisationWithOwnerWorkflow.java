package com.example.empsched.workflow.worker;

import com.example.empsched.shared.dto.organisation.CreateOrganisationWithOwnerRequest;
import com.example.empsched.shared.dto.organisation.OrganisationResponse;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CreateOrganisationWithOwnerWorkflow {
    @WorkflowMethod
    OrganisationResponse create(final CreateOrganisationWithOwnerRequest request);
}
