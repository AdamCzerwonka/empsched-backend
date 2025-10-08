package com.example.empsched.workflow.controller;

import com.example.empsched.shared.dto.position.CreatePositionRequest;
import com.example.empsched.shared.dto.position.PositionResponse;
import com.example.empsched.workflow.util.RequestContext;
import com.example.empsched.workflow.util.Tasks;
import com.example.empsched.workflow.worker.CreatePositionWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/positions")
public class PositionsController {
    private final WorkflowClient client;

    @PostMapping
    public ResponseEntity<PositionResponse> createPosition(@RequestBody @Valid final CreatePositionRequest request) {
        final WorkflowOptions options = WorkflowOptions.newBuilder()
                .setWorkflowId(UUID.randomUUID().toString())
                .setTaskQueue(Tasks.TASK_QUEUE_POSITION_MANAGEMENT)
                .build();

        final CreatePositionWorkflow workflow = client.newWorkflowStub(CreatePositionWorkflow.class, options);
        final PositionResponse positionResponse = workflow.create(request, new RequestContext());

        return ResponseEntity.status(HttpStatus.CREATED).body(positionResponse);
    }
}
