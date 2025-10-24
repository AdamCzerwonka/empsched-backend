package com.example.empsched.employee.controller;

import com.example.empsched.employee.entity.Employee;
import com.example.empsched.employee.mapper.DtoMapper;
import com.example.empsched.employee.service.EmployeeService;
import com.example.empsched.employee.util.WorkflowTasks;
import com.example.empsched.employee.workflow.CreateEmployeeWorkflow;
import com.example.empsched.shared.dto.employee.CreateEmployeeRequest;
import com.example.empsched.shared.dto.employee.EmployeeResponse;
import com.example.empsched.shared.dto.page.FilterRequest;
import com.example.empsched.shared.dto.page.PagedResponse;
import com.example.empsched.shared.mapper.BaseMapper;
import com.example.empsched.shared.utils.CredentialsExtractor;
import com.example.empsched.shared.utils.RequestContext;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class EmployeeController {
    private final EmployeeService employeeService;
    private final DtoMapper mapper;
    private final BaseMapper baseMapper;
    private final WorkflowClient client;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<PagedResponse<EmployeeResponse>> getAllEmployees(final FilterRequest filterRequest) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        final Pageable pageable = baseMapper.mapToPageable(
                filterRequest,
                Sort.by("lastName").ascending().and(Sort.by("firstName").ascending()));

        final Page<Employee> employeesPage = employeeService.getAllEmployees(organisationId, pageable);
        return ResponseEntity.ok(baseMapper.mapToPagedResponse(employeesPage, mapper::mapToEmployeeResponse));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody @Valid CreateEmployeeRequest request) {
        final WorkflowOptions options = WorkflowOptions.newBuilder()
                .setWorkflowId(UUID.randomUUID().toString())
                .setTaskQueue(WorkflowTasks.TASK_QUEUE_EMPLOYEE_MANAGEMENT)
                .build();

        final CreateEmployeeWorkflow workflow = client.newWorkflowStub(CreateEmployeeWorkflow.class, options);
        final Employee employee = workflow.createEmployee(request, new RequestContext());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapToEmployeeResponse(employee));
    }

    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_ORGANISATION_ADMIN')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable final UUID employeeId) {
        final UUID organisationId = CredentialsExtractor.getOrganisationIdFromContext();
        employeeService.deleteEmployee(employeeId, organisationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
