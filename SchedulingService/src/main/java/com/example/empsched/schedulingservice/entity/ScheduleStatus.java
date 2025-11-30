package com.example.empsched.schedulingservice.entity;

public enum ScheduleStatus {
    DRAFT,      // Created, empty, or manually edited
    SOLVING,    // Currently being processed by Timefold
    SOLVED,     // Timefold finished, ready for review
    PUBLISHED   // Visible to employees
}