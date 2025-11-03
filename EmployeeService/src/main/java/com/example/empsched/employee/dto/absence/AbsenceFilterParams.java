package com.example.empsched.employee.dto.absence;

import com.example.empsched.shared.dto.page.FilterRequest;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AbsenceFilterParams extends FilterRequest {
    private LocalDate startFrom;
    private LocalDate startTo;
}
