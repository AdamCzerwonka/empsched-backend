-- ============================================================================
-- Test Data for Scheduling Service - Nurses and Doctors
-- ============================================================================

-- ============================================================================
-- 1. SCHEDULING_EMPLOYEE - Create 4 nurses and 2 doctors with skills
-- ============================================================================

-- Nurse 1: Anna Kowalska
INSERT INTO scheduling_employee (id, created_at, updated_at, max_weekly_hours, version)
VALUES ('bcd6153b-b92b-44fc-bd9a-7adc9e0a30e8', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 40, 0);

INSERT INTO scheduling_employee_skills (scheduling_employee_id, skills)
VALUES ('bcd6153b-b92b-44fc-bd9a-7adc9e0a30e8', 'Nurse');

-- Nurse 2: Barbara Lewandowska
INSERT INTO scheduling_employee (id, created_at, updated_at, max_weekly_hours, version)
VALUES ('24c0184f-87db-4c08-9541-e39b3e3f972c', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 40, 0);

INSERT INTO scheduling_employee_skills (scheduling_employee_id, skills)
VALUES ('24c0184f-87db-4c08-9541-e39b3e3f972c', 'Nurse');

-- Nurse 3: Ewa Nowak (can also do pediatric care)
INSERT INTO scheduling_employee (id, created_at, updated_at, max_weekly_hours, version)
VALUES ('ef7587a5-be5e-4853-9d63-2d3d3f9e54f2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 40, 0);

INSERT INTO scheduling_employee_skills (scheduling_employee_id, skills)
VALUES ('ef7587a5-be5e-4853-9d63-2d3d3f9e54f2', 'Nurse'),
       ('ef7587a5-be5e-4853-9d63-2d3d3f9e54f2', 'Pediatric Care');

-- Nurse 4: Joanna Szymczak (part-time)
INSERT INTO scheduling_employee (id, created_at, updated_at, max_weekly_hours, version)
VALUES ('f28fe10c-4902-4201-8834-909f3af1287a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 24, 0);

INSERT INTO scheduling_employee_skills (scheduling_employee_id, skills)
VALUES ('f28fe10c-4902-4201-8834-909f3af1287a', 'Nurse');

-- Doctor 1: Dr. Jan Kowalski
INSERT INTO scheduling_employee (id, created_at, updated_at, max_weekly_hours, version)
VALUES ('68641161-d653-4af4-b640-2114fd18d748', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 40, 0);

INSERT INTO scheduling_employee_skills (scheduling_employee_id, skills)
VALUES ('68641161-d653-4af4-b640-2114fd18d748', 'Doctor'),
       ('68641161-d653-4af4-b640-2114fd18d748', 'Pediatric Care');

-- Doctor 2: Dr. Maria Piotrowski
INSERT INTO scheduling_employee (id, created_at, updated_at, max_weekly_hours, version)
VALUES ('ee44815e-dfeb-489b-b9db-62b6e871353a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 40, 0);

INSERT INTO scheduling_employee_skills (scheduling_employee_id, skills)
VALUES ('ee44815e-dfeb-489b-b9db-62b6e871353a', 'Doctor');

-- ============================================================================
-- 2. SCHEDULE - Create a schedule for the week
-- ============================================================================

INSERT INTO schedule (id, created_at, updated_at, start_date, end_date, status, score, version)
VALUES ('971f8edc-5638-4780-bae4-173b92954ff8', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '2024-12-23', '2024-12-29', 'DRAFT', NULL, 0);

-- ============================================================================
-- 3. SHIFT - Create shifts for the schedule
-- ============================================================================

-- -- Monday (2024-12-23) Morning Shifts
-- INSERT INTO shift (id, created_at, updated_at, start_time, end_time, required_skill, assigned_employee_id, schedule_id)
-- VALUES
-- ('550e8400-e29b-41d4-a716-446655440201', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-23 08:00:00', '2024-12-23 16:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440202', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-23 08:00:00', '2024-12-23 16:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440203', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-23 08:00:00', '2024-12-23 16:00:00', 'Doctor', NULL, '550e8400-e29b-41d4-a716-446655440100');
--
-- -- Monday Evening Shifts
-- INSERT INTO shift (id, created_at, updated_at, start_time, end_time, required_skill, assigned_employee_id, schedule_id)
-- VALUES
-- ('550e8400-e29b-41d4-a716-446655440204', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-23 16:00:00', '2024-12-24 00:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440205', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-23 16:00:00', '2024-12-24 00:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100');
--
-- -- Tuesday (2024-12-24) Morning Shifts
-- INSERT INTO shift (id, created_at, updated_at, start_time, end_time, required_skill, assigned_employee_id, schedule_id)
-- VALUES
-- ('550e8400-e29b-41d4-a716-446655440206', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-24 08:00:00', '2024-12-24 16:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440207', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-24 08:00:00', '2024-12-24 16:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440208', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-24 08:00:00', '2024-12-24 16:00:00', 'Doctor', NULL, '550e8400-e29b-41d4-a716-446655440100');
--
-- -- Tuesday Evening Shifts
-- INSERT INTO shift (id, created_at, updated_at, start_time, end_time, required_skill, assigned_employee_id, schedule_id)
-- VALUES
-- ('550e8400-e29b-41d4-a716-446655440209', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-24 16:00:00', '2024-12-25 00:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440210', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-24 16:00:00', '2024-12-25 00:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100');
--
-- -- Wednesday (2024-12-25) Morning Shifts
-- INSERT INTO shift (id, created_at, updated_at, start_time, end_time, required_skill, assigned_employee_id, schedule_id)
-- VALUES
-- ('550e8400-e29b-41d4-a716-446655440211', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-25 08:00:00', '2024-12-25 16:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440212', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-25 08:00:00', '2024-12-25 16:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440213', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-25 08:00:00', '2024-12-25 16:00:00', 'Doctor', NULL, '550e8400-e29b-41d4-a716-446655440100');
--
-- -- Wednesday Evening Shifts
-- INSERT INTO shift (id, created_at, updated_at, start_time, end_time, required_skill, assigned_employee_id, schedule_id)
-- VALUES
-- ('550e8400-e29b-41d4-a716-446655440214', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-25 16:00:00', '2024-12-26 00:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440215', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-25 16:00:00', '2024-12-26 00:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100');
--
-- -- Thursday (2024-12-26) Morning Shifts
-- INSERT INTO shift (id, created_at, updated_at, start_time, end_time, required_skill, assigned_employee_id, schedule_id)
-- VALUES
-- ('550e8400-e29b-41d4-a716-446655440216', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-26 08:00:00', '2024-12-26 16:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440217', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-26 08:00:00', '2024-12-26 16:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440218', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-26 08:00:00', '2024-12-26 16:00:00', 'Doctor', NULL, '550e8400-e29b-41d4-a716-446655440100');
--
-- -- Thursday Evening Shifts
-- INSERT INTO shift (id, created_at, updated_at, start_time, end_time, required_skill, assigned_employee_id, schedule_id)
-- VALUES
-- ('550e8400-e29b-41d4-a716-446655440219', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-26 16:00:00', '2024-12-27 00:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440220', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-26 16:00:00', '2024-12-27 00:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100');
--
-- -- Friday (2024-12-27) Morning Shifts
-- INSERT INTO shift (id, created_at, updated_at, start_time, end_time, required_skill, assigned_employee_id, schedule_id)
-- VALUES
-- ('550e8400-e29b-41d4-a716-446655440221', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-27 08:00:00', '2024-12-27 16:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440222', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-27 08:00:00', '2024-12-27 16:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440223', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-27 08:00:00', '2024-12-27 16:00:00', 'Doctor', NULL, '550e8400-e29b-41d4-a716-446655440100');
--
-- -- Friday Evening Shifts
-- INSERT INTO shift (id, created_at, updated_at, start_time, end_time, required_skill, assigned_employee_id, schedule_id)
-- VALUES
-- ('550e8400-e29b-41d4-a716-446655440224', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-27 16:00:00', '2024-12-28 00:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440225', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-27 16:00:00', '2024-12-28 00:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100');
--
-- -- Saturday (2024-12-28) Morning Shifts
-- INSERT INTO shift (id, created_at, updated_at, start_time, end_time, required_skill, assigned_employee_id, schedule_id)
-- VALUES
-- ('550e8400-e29b-41d4-a716-446655440226', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-28 08:00:00', '2024-12-28 16:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440227', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-28 08:00:00', '2024-12-28 16:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440228', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-28 08:00:00', '2024-12-28 16:00:00', 'Doctor', NULL, '550e8400-e29b-41d4-a716-446655440100');
--
-- -- Saturday Evening Shifts
-- INSERT INTO shift (id, created_at, updated_at, start_time, end_time, required_skill, assigned_employee_id, schedule_id)
-- VALUES
-- ('550e8400-e29b-41d4-a716-446655440229', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-28 16:00:00', '2024-12-29 00:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440230', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-28 16:00:00', '2024-12-29 00:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100');
--
-- -- Sunday (2024-12-29) Morning Shifts
-- INSERT INTO shift (id, created_at, updated_at, start_time, end_time, required_skill, assigned_employee_id, schedule_id)
-- VALUES
-- ('550e8400-e29b-41d4-a716-446655440231', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-29 08:00:00', '2024-12-29 16:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440232', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-29 08:00:00', '2024-12-29 16:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440233', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-29 08:00:00', '2024-12-29 16:00:00', 'Doctor', NULL, '550e8400-e29b-41d4-a716-446655440100');
--
-- -- Sunday Evening Shifts
-- INSERT INTO shift (id, created_at, updated_at, start_time, end_time, required_skill, assigned_employee_id, schedule_id)
-- VALUES
-- ('550e8400-e29b-41d4-a716-446655440234', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-29 16:00:00', '2024-12-30 00:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100'),
--
-- ('550e8400-e29b-41d4-a716-446655440235', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
--  '2024-12-29 16:00:00', '2024-12-30 00:00:00', 'Nurse', NULL, '550e8400-e29b-41d4-a716-446655440100');

-- ============================================================================
-- 4. EMPLOYEE_AVAILABILITY - Add some availability constraints
-- ============================================================================

-- Anna: Unavailable on Monday (vacation)
INSERT INTO employee_availability (id, created_at, updated_at, employee_id, date, type, version)
VALUES ('e8133265-2ef3-4570-b7ae-0bc6004dfa80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        'bcd6153b-b92b-44fc-bd9a-7adc9e0a30e8', '2024-12-23', 'UNAVAILABLE', 0);

-- Barbara: Desires to work on Friday
INSERT INTO employee_availability (id, created_at, updated_at, employee_id, date, type, version)
VALUES ('3f43e638-cd8f-4e89-8d2a-8190331b7781', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '24c0184f-87db-4c08-9541-e39b3e3f972c', '2024-12-27', 'DESIRED', 0);

-- Ewa: Unavailable on Wednesday-Thursday (training)
INSERT INTO employee_availability (id, created_at, updated_at, employee_id, date, type, version)
VALUES ('6e887d86-cbcc-4d53-818d-133a4e9ec061', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        'ef7587a5-be5e-4853-9d63-2d3d3f9e54f2', '2024-12-25', 'UNAVAILABLE', 0),
       ('f63b9328-411a-4568-b35f-85230af59d89', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        'ef7587a5-be5e-4853-9d63-2d3d3f9e54f2', '2024-12-26', 'UNAVAILABLE', 0);

-- Joanna: Desires Sunday morning shift
INSERT INTO employee_availability (id, created_at, updated_at, employee_id, date, type, version)
VALUES ('5b0f783f-2ce6-46de-a721-1cf0deca0d46', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        'f28fe10c-4902-4201-8834-909f3af1287a', '2024-12-29', 'DESIRED', 0);

-- Dr. Kowalski: Desires Tuesday morning
INSERT INTO employee_availability (id, created_at, updated_at, employee_id, date, type, version)
VALUES ('ea82bdb7-60f7-4f25-a0bd-79b13ce4b7a9', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '68641161-d653-4af4-b640-2114fd18d748', '2024-12-24', 'DESIRED', 0);

-- ============================================================================
-- Summary:
-- - 4 Nurses (3 full-time, 1 part-time with 24h max/week)
-- - 2 Doctors (1 with pediatric skills)
-- - 7 days × 5 shifts/day = 35 total shifts to assign
-- - Availability constraints to make scheduling realistic
-- ============================================================================

