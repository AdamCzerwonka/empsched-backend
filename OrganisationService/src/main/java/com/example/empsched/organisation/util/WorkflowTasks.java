package com.example.empsched.organisation.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkflowTasks {
    public static final String WORKER_ORGANISATION_MANAGEMENT = "organisation-management-worker";
    public static final String TASK_QUEUE_ORGANISATION_MANAGEMENT = "TASK_QUEUE_ORGANISATION_MANAGEMENT";

    public static final String WORKER_POSITION_MANAGEMENT = "position-management-worker";
    public static final String TASK_QUEUE_POSITION_MANAGEMENT = "TASK_QUEUE_POSITION_MANAGEMENT";
}
