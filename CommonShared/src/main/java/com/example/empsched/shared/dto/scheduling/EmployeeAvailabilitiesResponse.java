package com.example.empsched.shared.dto.scheduling;

import java.util.List;

public record EmployeeAvailabilitiesResponse(
    List<EmployeeAvailabilityResponse> availabilities
){
}
