package com.example.empsched.employee.specification;

import com.example.empsched.employee.entity.Absence;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AbsenceSpecification {
    public static Specification<Absence> filterByEmployeeIdAndOverlappingDates(final @NonNull UUID employeeId, final @Nullable LocalDate startFrom, final @Nullable LocalDate startTo) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicates = criteriaBuilder.equal(root.get("employee").get("id"), employeeId);

            if (startFrom != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), startFrom));
            }

            if (startTo != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), startTo));
            }

            return predicates;
        };
    }

    public static Specification<Absence> filterByOrganisationAndDateAndApprovalStatus(final @NonNull UUID organisationId, final @Nullable LocalDate startFrom, final @Nullable LocalDate startTo, final boolean approved) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicates = criteriaBuilder.equal(root.get("employee").get("organisation").get("id"), organisationId);

            if (startFrom != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startFrom));
            }

            if (startTo != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), startTo));
            }

            predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("approved"), approved));

            return predicates;
        };
    }
}
