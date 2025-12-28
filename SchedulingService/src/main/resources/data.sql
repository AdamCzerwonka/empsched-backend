-- ============================================================================
-- Test Data for Scheduling Service - Nurses and Doctors
-- ============================================================================

-- ============================================================================
-- 0. organisation - Create one organisation used by all seeded data
-- ============================================================================

INSERT INTO organisation (id, created_at, updated_at, plan, version)
VALUES ('7123f3ec-3517-4d3e-98e2-4e98a4cd9581', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'DEPARTMENT', 0);

-- ============================================================================
-- 1. employee - Create 4 nurses and 2 doctors with skills
-- ============================================================================

-- Nurse 1: Anna Kowalska
INSERT INTO employee (id, created_at, updated_at, max_weekly_hours, organisation_id, version)
VALUES ('bcd6153b-b92b-44fc-bd9a-7adc9e0a30e8', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 40, '7123f3ec-3517-4d3e-98e2-4e98a4cd9581', 0);

-- Nurse 2: Barbara Lewandowska
INSERT INTO employee (id, created_at, updated_at, max_weekly_hours, organisation_id, version)
VALUES ('24c0184f-87db-4c08-9541-e39b3e3f972c', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 40, '7123f3ec-3517-4d3e-98e2-4e98a4cd9581', 0);

-- Nurse 3: Ewa Nowak (can also do pediatric care)
INSERT INTO employee (id, created_at, updated_at, max_weekly_hours, organisation_id, version)
VALUES ('ef7587a5-be5e-4853-9d63-2d3d3f9e54f2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 40, '7123f3ec-3517-4d3e-98e2-4e98a4cd9581', 0);

-- Nurse 4: Joanna Szymczak (part-time)
INSERT INTO employee (id, created_at, updated_at, max_weekly_hours, organisation_id, version)
VALUES ('f28fe10c-4902-4201-8834-909f3af1287a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 24, '7123f3ec-3517-4d3e-98e2-4e98a4cd9581', 0);

-- Doctor 1: Dr. Jan Kowalski
INSERT INTO employee (id, created_at, updated_at, max_weekly_hours, organisation_id, version)
VALUES ('68641161-d653-4af4-b640-2114fd18d748', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 40, '7123f3ec-3517-4d3e-98e2-4e98a4cd9581', 0);

-- Doctor 2: Dr. Maria Piotrowski
INSERT INTO employee (id, created_at, updated_at, max_weekly_hours, organisation_id, version)
VALUES ('ee44815e-dfeb-489b-b9db-62b6e871353a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 40, '7123f3ec-3517-4d3e-98e2-4e98a4cd9581', 0);

-- ============================================================================
-- 1.a Positions - create Position entities (replaces skills strings)
-- ============================================================================

-- Using the same organisation UUID for seeded positions
INSERT INTO position (id, created_at, updated_at, position_name, organisation_id, version)
VALUES
('2a097638-8d8f-431b-a8cb-3b51ea6e0c6b', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Nurse', '7123f3ec-3517-4d3e-98e2-4e98a4cd9581', 0),
('4a00246d-69c7-4724-9e76-aae5ff62f320', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Doctor', '7123f3ec-3517-4d3e-98e2-4e98a4cd9581', 0),
('72144d96-5373-4487-853f-c8a5bfd489e7', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Pediatric Care', '7123f3ec-3517-4d3e-98e2-4e98a4cd9581', 0);

-- ============================================================================
-- 1.b employee_position join table - link employees to positions
-- NOTE: The ManyToMany mapping uses a join table named 'employee_position' (owner property 'positions'),
-- which stores (employee_id, position_id).
-- ============================================================================

-- Anna (Nurse)
INSERT INTO employee_position (employee_id, position_id)
VALUES ('bcd6153b-b92b-44fc-bd9a-7adc9e0a30e8', '2a097638-8d8f-431b-a8cb-3b51ea6e0c6b');

-- Barbara (Nurse)
INSERT INTO employee_position (employee_id, position_id)
VALUES ('24c0184f-87db-4c08-9541-e39b3e3f972c', '2a097638-8d8f-431b-a8cb-3b51ea6e0c6b');

-- Ewa (Nurse, Pediatric Care)
INSERT INTO employee_position (employee_id, position_id)
VALUES ('ef7587a5-be5e-4853-9d63-2d3d3f9e54f2', '2a097638-8d8f-431b-a8cb-3b51ea6e0c6b'),
       ('ef7587a5-be5e-4853-9d63-2d3d3f9e54f2', '72144d96-5373-4487-853f-c8a5bfd489e7');

-- Joanna (Nurse)
INSERT INTO employee_position (employee_id, position_id)
VALUES ('f28fe10c-4902-4201-8834-909f3af1287a', '2a097638-8d8f-431b-a8cb-3b51ea6e0c6b');

-- Dr. Kowalski (Doctor, Pediatric Care)
INSERT INTO employee_position (employee_id, position_id)
VALUES ('68641161-d653-4af4-b640-2114fd18d748', '4a00246d-69c7-4724-9e76-aae5ff62f320'),
       ('68641161-d653-4af4-b640-2114fd18d748', '72144d96-5373-4487-853f-c8a5bfd489e7');

-- Dr. Piotrowski (Doctor)
INSERT INTO employee_position (employee_id, position_id)
VALUES ('ee44815e-dfeb-489b-b9db-62b6e871353a', '4a00246d-69c7-4724-9e76-aae5ff62f320');

-- ============================================================================
-- 4. EMPLOYEE_AVAILABILITY - Add some availability constraints
-- ============================================================================

-- Anna: Unavailable on Monday (vacation)
INSERT INTO employee_availability (id, created_at, updated_at, absence_id, employee_id, date, type, version)
VALUES ('e8133265-2ef3-4570-b7ae-0bc6004dfa80', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '12a850fe-5e45-44b1-b622-51e0317822cc',
        'bcd6153b-b92b-44fc-bd9a-7adc9e0a30e8', '2024-12-23', 'UNAVAILABLE', 0);

-- Barbara: Desires to work on Friday
INSERT INTO employee_availability (id, created_at, updated_at, absence_id, employee_id, date, type, version)
VALUES ('3f43e638-cd8f-4e89-8d2a-8190331b7781', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '90e4917c-61af-4aad-95c2-8ecd148f7428',
        '24c0184f-87db-4c08-9541-e39b3e3f972c', '2024-12-27', 'DESIRED', 0);

-- Ewa: Unavailable on Wednesday-Thursday (training)
INSERT INTO employee_availability (id, created_at, updated_at, absence_id, employee_id, date, type, version)
VALUES ('6e887d86-cbcc-4d53-818d-133a4e9ec061', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '6cee80ca-3734-40e2-a156-8d05c43b41de',
        'ef7587a5-be5e-4853-9d63-2d3d3f9e54f2', '2024-12-25', 'UNAVAILABLE', 0),
       ('f63b9328-411a-4568-b35f-85230af59d89', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '6cee80ca-3734-40e2-a156-8d05c43b41de',
        'ef7587a5-be5e-4853-9d63-2d3d3f9e54f2', '2024-12-26', 'UNAVAILABLE', 0);

-- Joanna: Desires Sunday morning shift
INSERT INTO employee_availability (id, created_at, updated_at, absence_id, employee_id, date, type, version)
VALUES ('5b0f783f-2ce6-46de-a721-1cf0deca0d46', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'e8133265-2ef3-4570-b7ae-0bc6004dfa80',
        'f28fe10c-4902-4201-8834-909f3af1287a', '2024-12-29', 'DESIRED', 0);

-- Dr. Kowalski: Desires Tuesday morning
INSERT INTO employee_availability (id, created_at, updated_at, absence_id, employee_id, date, type, version)
VALUES ('ea82bdb7-60f7-4f25-a0bd-79b13ce4b7a9', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'a352c84d-ef68-4d0c-9da5-16b1a738f509',
        '68641161-d653-4af4-b640-2114fd18d748', '2024-12-24', 'DESIRED', 0);

-- ============================================================================
-- Summary:
-- - 4 Nurses (3 full-time, 1 part-time with 24h max/week)
-- - 2 Doctors (1 with pediatric skills)
-- - 7 days × 5 shifts/day = 35 total shifts to assign
-- - Availability constraints to make scheduling realistic
-- ============================================================================

