INSERT INTO public.auth_user (created_at, updated_at, version, id, email, password)
VALUES ('2025-06-16 18:15:41.185468', '2025-06-16 18:15:41.185468', 0, 'c8798fd9-6ff8-4a66-8d43-18463d79b0bb',
        'admin@admin.com', '$2a$10$9R4ST82ob/E0uqIdUvdZEuqjZC68/14d28M1x81J80wJI4Bi3sJFi'),
 ('2025-06-16 18:15:41.185468', '2025-06-16 18:15:41.185468', 0, '771b3967-aef4-4b7b-9770-d123e4fe87fc',
        'admin1@admin.com', '$2a$10$9R4ST82ob/E0uqIdUvdZEuqjZC68/14d28M1x81J80wJI4Bi3sJFi'),
 ('2025-06-16 18:15:41.185468', '2025-06-16 18:15:41.185468', 0, '4ae525e7-87be-4fc7-aad2-36e7c1fcc68f',
        'admin2@admin.com', '$2a$10$9R4ST82ob/E0uqIdUvdZEuqjZC68/14d28M1x81J80wJI4Bi3sJFi'),
 ('2025-06-16 18:15:41.185468', '2025-06-16 18:15:41.185468', 0, '5005db5d-c509-4c37-aa8e-1ecb48c4df5c',
        'admin3@admin.com', '$2a$10$9R4ST82ob/E0uqIdUvdZEuqjZC68/14d28M1x81J80wJI4Bi3sJFi'),
 ('2025-06-16 18:15:41.185468', '2025-06-16 18:15:41.185468', 0, '0b123b66-e7a0-49e0-acb9-1559d5635bf6',
        'admin4@admin.com', '$2a$10$9R4ST82ob/E0uqIdUvdZEuqjZC68/14d28M1x81J80wJI4Bi3sJFi');

INSERT INTO public.user_roles (user_id, role)
VALUES ('c8798fd9-6ff8-4a66-8d43-18463d79b0bb', 'ADMIN'),
 ('771b3967-aef4-4b7b-9770-d123e4fe87fc', 'ADMIN'),
 ('4ae525e7-87be-4fc7-aad2-36e7c1fcc68f', 'ADMIN'),
 ('5005db5d-c509-4c37-aa8e-1ecb48c4df5c', 'ADMIN'),
 ('0b123b66-e7a0-49e0-acb9-1559d5635bf6', 'ADMIN');

