package com.example.empsched.schedulingservice.repository;

import com.example.empsched.schedulingservice.entity.SchedulingEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SchedulingEmployeeRepository extends JpaRepository<SchedulingEmployee, UUID> {
}
