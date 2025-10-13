package com.example.empsched.workflow.controller;

import com.example.empsched.shared.dto.employee.CreateEmployeeRequest;
import com.example.empsched.shared.dto.employee.EmployeeResponse;
import com.example.empsched.workflow.util.RequestContext;
import com.example.empsched.workflow.util.Tasks;
import com.example.empsched.workflow.worker.CreateEmployeeWorkflow;
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
@RequestMapping("/employees")
public class EmployeeController {
    private final WorkflowClient client;

    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@RequestBody @Valid final CreateEmployeeRequest request) {
        final WorkflowOptions options = WorkflowOptions.newBuilder()
                .setWorkflowId(UUID.randomUUID().toString())
                .setTaskQueue(Tasks.TASK_QUEUE_EMPLOYEE_MANAGEMENT)
                .build();

        final CreateEmployeeWorkflow workflow = client.newWorkflowStub(CreateEmployeeWorkflow.class, options);
        final EmployeeResponse response = workflow.createEmployee(request, new RequestContext());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
