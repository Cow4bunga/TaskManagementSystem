package com.ilevitsky.testproject.tasksystem.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskInitializer {

  @Autowired private JdbcTemplate jdbcTemplate;

  public void initTasks() {
    insertTasks();
    insertComments();
  }

  private void insertTasks() {
    String sql =
        "INSERT INTO public.tasks (title, description, status, priority, creator_id, assignee_id) "
            + "VALUES (?, ?, ?, ?, (SELECT id FROM public.users WHERE email = ?), (SELECT id FROM public.users WHERE email = ?))";

    insertTaskIfNotExists(
        "Prepare Presentation",
        "Create an engaging presentation for the upcoming team meeting focusing on Q4 results.",
        "PENDING",
        "MEDIUM",
        "admin1@gmail.com",
        "user1@gmail.com",
        sql);

    insertTaskIfNotExists(
        "Develop New Feature",
        "Implement the new user authentication feature, including social media login options.",
        "IN_PROCESS",
        "HIGH",
        "admin2@gmail.com",
        "user2@gmail.com",
        sql);

    insertTaskIfNotExists(
        "Bug Fixing Sprint",
        "Resolve critical bugs reported in the last release and ensure stability.",
        "COMPLETED",
        "HIGH",
        "admin3@gmail.com",
        "user3@gmail.com",
        sql);

    insertTaskIfNotExists(
        "Market Research",
        "Conduct thorough market research to identify potential areas for product expansion.",
        "PENDING",
        "MEDIUM",
        "admin1@gmail.com",
        "user4@gmail.com",
        sql);

    insertTaskIfNotExists(
        "Team Building Activity",
        "Organize a fun team-building event to enhance collaboration and morale.",
        "PENDING",
        "LOW",
        "admin2@gmail.com",
        "user5@gmail.com",
        sql);

    insertTaskIfNotExists(
        "Code Review",
        "Review pull requests and provide constructive feedback to improve code quality.",
        "IN_PROCESS",
        "MEDIUM",
        "admin3@gmail.com",
        "user6@gmail.com",
        sql);

    insertTaskIfNotExists(
        "Update Documentation",
        "Revise and update the project documentation to reflect recent changes and improvements.",
        "COMPLETED",
        "LOW",
        "admin1@gmail.com",
        "user7@gmail.com",
        sql);

    insertTaskIfNotExists(
        "Client Feedback Session",
        "Gather feedback from the client on the latest project deliverables.",
        "PENDING",
        "MEDIUM",
        "admin2@gmail.com",
        "user8@gmail.com",
        sql);

    insertTaskIfNotExists(
        "Launch Marketing Campaign",
        "Prepare and launch the new marketing campaign targeting potential customers.",
        "IN_PROCESS",
        "HIGH",
        "admin3@gmail.com",
        "user9@gmail.com",
        sql);

    insertTaskIfNotExists(
        "Finalize Budget Report",
        "Complete the budget report for the upcoming fiscal year and present it to management.",
        "COMPLETED",
        "MEDIUM",
        "admin1@gmail.com",
        "user10@gmail.com",
        sql);
  }

  private void insertTaskIfNotExists(
      String title,
      String description,
      String status,
      String priority,
      String creatorEmail,
      String assigneeEmail,
      String sql) {
    String checkSql = "SELECT COUNT(*) FROM public.tasks WHERE title = ?";
    Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, title);

    if (count == null || count == 0) {
      jdbcTemplate.update(sql, title, description, status, priority, creatorEmail, assigneeEmail);
    }
  }

  private void insertComments() {
    String sql =
        "INSERT INTO public.task_comments (task_id, comment) "
            + "VALUES ((SELECT id FROM public.tasks WHERE title = ?), ?)";

    insertCommentIfNotExists(
        "Prepare Presentation", "This needs to be visually appealing and data-driven.");
    insertCommentIfNotExists(
        "Prepare Presentation", "Don’t forget to rehearse before the meeting.");
    insertCommentIfNotExists(
        "Develop New Feature", "Make sure to test the social login thoroughly.");
    insertCommentIfNotExists("Bug Fixing Sprint", "Prioritize the bugs based on user impact.");
    insertCommentIfNotExists("Market Research", "Include competitor analysis in the report.");
    insertCommentIfNotExists(
        "Team Building Activity", "Consider activities that encourage team interaction.");
    insertCommentIfNotExists("Code Review", "Focus on code efficiency and readability.");
    insertCommentIfNotExists(
        "Client Feedback Session", "Prepare a list of questions to guide the discussion.");
    insertCommentIfNotExists(
        "Launch Marketing Campaign", "Schedule a meeting to brainstorm creative ideas.");
  }

  private void insertCommentIfNotExists(String taskTitle, String comment) {
    String checkSql =
        "SELECT COUNT(*) FROM public.task_comments WHERE comment = ? "
            + "AND task_id = (SELECT id FROM public.tasks WHERE title = ?)";
    Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, comment, taskTitle);

    if (count == null || count == 0) {
      jdbcTemplate.update(
          "INSERT INTO public.task_comments (task_id, comment) VALUES "
              + "((SELECT id FROM public.tasks WHERE title = ?), ?)",
          taskTitle,
          comment);
    }
  }
}
