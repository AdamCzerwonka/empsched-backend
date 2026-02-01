-- ============================================================================
-- Test Data for Auth Service - Owner User
-- ============================================================================

-- Owner user for the test organisation
-- Password: owner123 (BCrypt hash)
-- Email: owner@hospital.com
-- This user owns the organisation with ID: 7123f3ec-3517-4d3e-98e2-4e98a4cd9581
INSERT INTO auth_user (id, created_at, updated_at, email, password, organisation_id, version)
VALUES ('a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        'owner@hospital.com',
        '$2a$10$ABL3q7AeeaGXkNz5x6TkWusFiavTf2NJ84qJFA9rq59NB.APNAYLC',
        '7123f3ec-3517-4d3e-98e2-4e98a4cd9581',
        0);

-- Assign ORGANISATION_ADMIN role to the user (owner of organisation)
INSERT INTO user_roles (user_id, role)
VALUES ('a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d', 'ORGANISATION_ADMIN');
