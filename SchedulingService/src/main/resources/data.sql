-- ============================================================================
-- Test Data for Scheduling Service - Nurses and Doctors
-- ============================================================================

-- ============================================================================
-- 0. organisation - Create one organisation used by all seeded data
-- ============================================================================

INSERT INTO organisation (id, created_at, updated_at, plan, version)
VALUES ('7123f3ec-3517-4d3e-98e2-4e98a4cd9581', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'DEPARTMENT', 0);

-- Data from generate_test_data.py goes here ->

