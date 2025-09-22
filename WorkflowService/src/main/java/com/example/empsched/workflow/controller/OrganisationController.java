package com.example.empsched.workflow.controller;

import com.example.empsched.shared.dto.organisation.CreateOrganisationWithOwnerRequest;
import com.example.empsched.workflow.util.Tasks;
import com.example.empsched.workflow.worker.CreateOrganisationWithOwnerWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organisations")
@Slf4j
public class OrganisationController {
    private final WorkflowClient client;

    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody @Valid final CreateOrganisationWithOwnerRequest request) {
        final WorkflowOptions options = WorkflowOptions.newBuilder()
                .setWorkflowId(UUID.randomUUID().toString())
                .setTaskQueue(Tasks.TASK_QUEUE_CREATE_ORGANISATION)
                .build();

        final CreateOrganisationWithOwnerWorkflow workflow = client.newWorkflowStub(CreateOrganisationWithOwnerWorkflow.class, options);
        workflow.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
