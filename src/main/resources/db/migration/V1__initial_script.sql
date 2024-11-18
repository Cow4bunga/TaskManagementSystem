-- V1 Initial script

BEGIN;

CREATE TABLE IF NOT EXISTS public.users
(
    id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email    VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    role     VARCHAR(10) NOT NULL CHECK (role IN ('ADMIN', 'USER'))
    );

CREATE INDEX IF NOT EXISTS idx_users_email ON public.users (email);

CREATE TABLE IF NOT EXISTS public.tasks
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title       VARCHAR(32) NOT NULL,
    description VARCHAR(128),
    status      VARCHAR(10) CHECK (status IN ('PENDING', 'IN_PROCESS', 'COMPLETED')) DEFAULT 'PENDING',
    priority    VARCHAR(10) CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')) DEFAULT 'LOW',
    creator_id  UUID NOT NULL,
    assignee_id UUID,

    CONSTRAINT fk_creator FOREIGN KEY (creator_id) REFERENCES public.users (id) ON DELETE CASCADE,
    CONSTRAINT fk_assignee FOREIGN KEY (assignee_id) REFERENCES public.users (id) ON DELETE SET NULL
    );

CREATE INDEX IF NOT EXISTS idx_tasks_title ON public.tasks (title);

CREATE TABLE IF NOT EXISTS public.task_comments
(
    task_id  UUID,
    index    BIGINT,
    comment  VARCHAR(255),

    CONSTRAINT fk_task FOREIGN KEY (task_id) REFERENCES public.tasks (id) ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_comments_task_id ON public.task_comments (task_id);

COMMIT;