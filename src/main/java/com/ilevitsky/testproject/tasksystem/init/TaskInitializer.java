package com.ilevitsky.testproject.tasksystem.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskInitializer {

  @Autowired private JdbcTemplate jdbcTemplate;

  public void initTasks() {
    jdbcTemplate.execute("TRUNCATE TABLE public.tasks CASCADE");
    jdbcTemplate.execute("TRUNCATE TABLE public.task_comments CASCADE");
    insertTasks();
    insertComments();
  }

  private void insertTasks() {
    String sql =
        "INSERT INTO public.tasks (title, description, status, priority, creator_id, assignee_id) VALUES (?, ?, ?, ?, (SELECT id FROM public.users WHERE email = ?), (SELECT id FROM public.users WHERE email = ?))";

    jdbcTemplate.update(
        sql,
        "Prepare Presentation",
        "Create an engaging presentation for the upcoming team meeting focusing on Q4 results.",
        "PENDING",
        "MEDIUM",
        "admin1@gmail.com",
        "user1@gmail.com");
    jdbcTemplate.update(
        sql,
        "Develop New Feature",
        "Implement the new user authentication feature, including social media login options.",
        "IN_PROCESS",
        "HIGH",
        "admin2@gmail.com",
        "user2@gmail.com");
    jdbcTemplate.update(
        sql,
        "Bug Fixing Sprint",
        "Resolve critical bugs reported in the last release and ensure stability.",
        "COMPLETED",
        "HIGH",
        "admin3@gmail.com",
        "user3@gmail.com");
    jdbcTemplate.update(
        sql,
        "Market Research",
        "Conduct thorough market research to identify potential areas for product expansion.",
        "PENDING",
        "MEDIUM",
        "admin1@gmail.com",
        "user4@gmail.com");
    jdbcTemplate.update(
        sql,
        "Team Building Activity",
        "Organize a fun team-building event to enhance collaboration and morale.",
        "PENDING",
        "LOW",
        "admin2@gmail.com",
        "user5@gmail.com");
    jdbcTemplate.update(
        sql,
        "Code Review",
        "Review pull requests and provide constructive feedback to improve code quality.",
        "IN_PROCESS",
        "MEDIUM",
        "admin3@gmail.com",
        "user6@gmail.com");
    jdbcTemplate.update(
        sql,
        "Update Documentation",
        "Revise and update the project documentation to reflect recent changes and improvements.",
        "COMPLETED",
        "LOW",
        "admin1@gmail.com",
        "user7@gmail.com");
    jdbcTemplate.update(
        sql,
        "Client Feedback Session",
        "Gather feedback from the client on the latest project deliverables.",
        "PENDING",
        "MEDIUM",
        "admin2@gmail.com",
        "user8@gmail.com");
    jdbcTemplate.update(
        sql,
        "Launch Marketing Campaign",
        "Prepare and launch the new marketing campaign targeting potential customers.",
        "IN_PROCESS",
        "HIGH",
        "admin3@gmail.com",
        "user9@gmail.com");
    jdbcTemplate.update(
        sql,
        "Finalize Budget Report",
        "Complete the budget report for the upcoming fiscal year and present it to management.",
        "COMPLETED",
        "MEDIUM",
        "admin1@gmail.com",
        "user10@gmail.com");
  }

  private void insertComments() {
    String sql =
        "INSERT INTO public.task_comments (task_id, comment) VALUES ((SELECT id FROM public.tasks WHERE title = ?), ?)";

    jdbcTemplate.update(
        sql, "Prepare Presentation", "This needs to be visually appealing and data-driven.");
    jdbcTemplate.update(
        sql, "Prepare Presentation", "Don’t forget to rehearse before the meeting.");
    jdbcTemplate.update(
        sql, "Develop New Feature", "Make sure to test the social login thoroughly.");
    jdbcTemplate.update(sql, "Bug Fixing Sprint", "Prioritize the bugs based on user impact.");
    jdbcTemplate.update(sql, "Market Research", "Include competitor analysis in the report.");
    jdbcTemplate.update(
        sql, "Team Building Activity", "Consider activities that encourage team interaction.");
    jdbcTemplate.update(sql, "Code Review", "Focus on code efficiency and readability.");
    jdbcTemplate.update(
        sql, "Client Feedback Session", "Prepare a list of questions to guide the discussion.");
    jdbcTemplate.update(
        sql, "Launch Marketing Campaign", "Schedule a meeting to brainstorm creative ideas.");
  }
}
