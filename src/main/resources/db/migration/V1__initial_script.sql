-- V1 Initial script

BEGIN;

CREATE TABLE public.tasks
(
    id          UUID PRIMARY KEY                                                     DEFAULT gen_random_uuid(),
    title       VARCHAR(32) NOT NULL,
    description VARCHAR(128),
    status      VARCHAR(10) CHECK (status IN ('Pending', 'In process', 'Completed')) DEFAULT 'Pending',
    priority    VARCHAR(10) CHECK (priority IN ('Low', 'Medium', 'High'))            DEFAULT 'Low'
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