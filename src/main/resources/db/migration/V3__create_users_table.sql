-- V3 create users table

BEGIN;

CREATE TABLE public.users
(
    id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email    VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    role     VARCHAR(10) NOT NULL CHECK (role IN ('ADMIN', 'USER'))
);

CREATE INDEX idx_users_email ON public.users (email);

COMMIT;