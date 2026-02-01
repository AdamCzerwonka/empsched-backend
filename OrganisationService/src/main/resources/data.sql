-- ============================================================================
-- Test Data for Organisation Service
-- ============================================================================

-- Organisation: Hospital Test Organisation
-- Owner: a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d (owner@hospital.com)
INSERT INTO organisation (id, created_at, updated_at, name, owner_id, plan, version)
VALUES ('7123f3ec-3517-4d3e-98e2-4e98a4cd9581',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        'Hospital Test Organisation',
        'a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d',
        'DEPARTMENT',
        0);

