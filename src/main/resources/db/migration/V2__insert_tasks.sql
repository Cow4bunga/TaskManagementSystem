-- V2 Data Insertion

BEGIN;

INSERT INTO public.tasks (title, description, status, priority)
VALUES ('Prepare Presentation', 'Create an engaging presentation for the upcoming team meeting focusing on Q4 results.',
        'PENDING', 'MEDIUM'),
       ('Develop New Feature', 'Implement the new user authentication feature, including social media login options.',
        'IN_PROCESS', 'HIGH'),
       ('Bug Fixing Sprint', 'Resolve critical bugs reported in the last release and ensure stability.',
        'COMPLETED', 'HIGH'),
       ('Market Research', 'Conduct thorough market research to identify potential areas for product expansion.',
        'PENDING', 'MEDIUM'),
       ('Team Building Activity', 'Organize a fun team-building event to enhance collaboration and morale.',
        'PENDING', 'LOW'),
       ('Code Review', 'Review pull requests and provide constructive feedback to improve code quality.',
        'IN_PROCESS', 'MEDIUM'),
       ('Update Documentation',
        'Revise and update the project documentation to reflect recent changes and improvements.',
        'COMPLETED', 'LOW'),
       ('Client Feedback Session', 'Gather feedback from the client on the latest project deliverables.',
        'PENDING', 'MEDIUM'),
       ('Launch Marketing Campaign', 'Prepare and launch the new marketing campaign targeting potential customers.',
        'IN_PROCESS', 'HIGH'),
       ('Finalize Budget Report',
        'Complete the budget report for the upcoming fiscal year and present it to management.',
        'COMPLETED', 'MEDIUM');

-- Insert comments for some tasks
INSERT INTO public.task_comments (task_id, comment)
VALUES ((SELECT id FROM public.tasks WHERE title = 'Prepare Presentation'),
        'This needs to be visually appealing and data-driven.'),
       ((SELECT id FROM public.tasks WHERE title = 'Prepare Presentation'),
        'Don’t forget to rehearse before the meeting.'),
       ((SELECT id FROM public.tasks WHERE title = 'Develop New Feature'),
        'Make sure to test the social login thoroughly.'),
       ((SELECT id FROM public.tasks WHERE title = 'Bug Fixing Sprint'),
        'Prioritize the bugs based on user impact.'),
       ((SELECT id FROM public.tasks WHERE title = 'Market Research'),
        'Include competitor analysis in the report.'),
       ((SELECT id FROM public.tasks WHERE title = 'Team Building Activity'),
        'Consider activities that encourage team interaction.'),
       ((SELECT id FROM public.tasks WHERE title = 'Code Review'),
        'Focus on code efficiency and readability.'),
       ((SELECT id FROM public.tasks WHERE title = 'Client Feedback Session'),
        'Prepare a list of questions to guide the discussion.'),
       ((SELECT id FROM public.tasks WHERE title = 'Launch Marketing Campaign'),
        'Schedule a meeting to brainstorm creative ideas.');

COMMIT;