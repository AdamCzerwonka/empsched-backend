package com.example.empsched.shared.dto.position;

import java.io.Serializable;
import java.util.UUID;

public record EmployeePositionChangeRequest(UUID employeeId, UUID positionId) implements Serializable {
}
