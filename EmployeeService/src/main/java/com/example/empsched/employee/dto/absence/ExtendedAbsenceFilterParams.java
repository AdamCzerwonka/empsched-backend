package com.example.empsched.employee.dto.absence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExtendedAbsenceFilterParams extends AbsenceFilterParams {
    private boolean approved;
}
