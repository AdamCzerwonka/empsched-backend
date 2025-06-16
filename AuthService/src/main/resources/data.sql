INSERT INTO public.auth_user (created_at, updated_at, version, id, email, password)
VALUES ('2025-06-16 18:15:41.185468', '2025-06-16 18:15:41.185468', 0, 'c8798fd9-6ff8-4a66-8d43-18463d79b0bb',
        'admin@admin.com', '$2a$10$9R4ST82ob/E0uqIdUvdZEuqjZC68/14d28M1x81J80wJI4Bi3sJFi');

INSERT INTO public.user_roles (user_id, role)
VALUES ('c8798fd9-6ff8-4a66-8d43-18463d79b0bb', 'ADMIN')
