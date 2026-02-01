package com.example.empsched.scheduling.repository;

import com.example.empsched.scheduling.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    Page<Schedule> findAllByOrganisationId(UUID organisationId, Pageable pageable);
}
