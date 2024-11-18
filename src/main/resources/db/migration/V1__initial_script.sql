-- V1 Initial script

BEGIN;

CREATE TABLE public.tasks
(
    id          UUID PRIMARY KEY                                                     DEFAULT gen_random_uuid(),
    title       VARCHAR(32) NOT NULL,
    description VARCHAR(128),
    status      VARCHAR(10) CHECK (status IN ('PENDING', 'IN_PROCESS', 'COMPLETED')) DEFAULT 'PENDING',
    priority    VARCHAR(10) CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH'))            DEFAULT 'LOW'
);

CREATE INDEX idx_tasks_title ON public.tasks (title);

CREATE TABLE public.task_comments
(
    task_id  UUID,
    index    BIGINT,
    comment VARCHAR(255)
);

CREATE INDEX idx_comments_task_id ON public.task_comments (task_id);

COMMIT;