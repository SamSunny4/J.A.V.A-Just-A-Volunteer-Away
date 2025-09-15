package com.java.volunteer.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.volunteer.dao.TaskDao;
import com.java.volunteer.model.Task;
import com.java.volunteer.util.DatabaseConnection;

/**
 * Implementation of the TaskDao interface
 */
public class TaskDaoImpl implements TaskDao {
    private static final Logger logger = LoggerFactory.getLogger(TaskDaoImpl.class);

    @Override
    public Task save(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (title, description, requester_id, volunteer_id, status, location, " +
                "scheduled_date, scheduled_time, estimated_duration, urgency_level) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setInt(3, task.getRequesterId());
            
            if (task.getVolunteerId() != null) {
                stmt.setInt(4, task.getVolunteerId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setString(5, task.getStatus());
            stmt.setString(6, task.getLocation());
            stmt.setDate(7, Date.valueOf(task.getScheduledDate()));
            stmt.setTime(8, Time.valueOf(task.getScheduledTime()));
            stmt.setInt(9, task.getEstimatedDuration());
            stmt.setString(10, task.getUrgencyLevel());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating task failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    task.setTaskId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating task failed, no ID obtained.");
                }
            }
            
            logger.info("Task created with ID: {}", task.getTaskId());
            return task;
        } catch (SQLException e) {
            logger.error("Error creating task: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Task findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE task_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTask(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding task by ID: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Task> findAll() throws SQLException {
        String sql = "SELECT * FROM tasks";
        List<Task> tasks = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
            
            return tasks;
        } catch (SQLException e) {
            logger.error("Error finding all tasks: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Task update(Task task) throws SQLException {
        String sql = "UPDATE tasks SET title = ?, description = ?, requester_id = ?, volunteer_id = ?, " +
                "status = ?, location = ?, scheduled_date = ?, scheduled_time = ?, estimated_duration = ?, " +
                "urgency_level = ?, previous_volunteer_id = ?, reassignment_reason = ? " +
                "WHERE task_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setInt(3, task.getRequesterId());
            
            if (task.getVolunteerId() != null) {
                stmt.setInt(4, task.getVolunteerId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setString(5, task.getStatus());
            stmt.setString(6, task.getLocation());
            stmt.setDate(7, Date.valueOf(task.getScheduledDate()));
            stmt.setTime(8, Time.valueOf(task.getScheduledTime()));
            stmt.setInt(9, task.getEstimatedDuration());
            stmt.setString(10, task.getUrgencyLevel());
            
            if (task.getPreviousVolunteerId() != null) {
                stmt.setInt(11, task.getPreviousVolunteerId());
            } else {
                stmt.setNull(11, Types.INTEGER);
            }
            
            stmt.setString(12, task.getReassignmentReason());
            stmt.setInt(13, task.getTaskId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating task failed, no rows affected.");
            }
            
            logger.info("Task updated with ID: {}", task.getTaskId());
            return task;
        } catch (SQLException e) {
            logger.error("Error updating task: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE task_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            
            logger.info("Task deleted with ID: {}, rows affected: {}", id, affectedRows);
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error deleting task: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Task> findByRequesterId(int requesterId) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE requester_id = ?";
        List<Task> tasks = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, requesterId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
            
            return tasks;
        } catch (SQLException e) {
            logger.error("Error finding tasks by requester ID: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Task> findByVolunteerId(int volunteerId) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE volunteer_id = ?";
        List<Task> tasks = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, volunteerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
            
            return tasks;
        } catch (SQLException e) {
            logger.error("Error finding tasks by volunteer ID: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Task> findByStatus(String status) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE status = ?";
        List<Task> tasks = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
            
            return tasks;
        } catch (SQLException e) {
            logger.error("Error finding tasks by status: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Task> findByScheduledDate(LocalDate date) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE scheduled_date = ?";
        List<Task> tasks = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
            
            return tasks;
        } catch (SQLException e) {
            logger.error("Error finding tasks by scheduled date: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean updateStatus(int taskId, String status) throws SQLException {
        String sql = "UPDATE tasks SET status = ? WHERE task_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, taskId);
            
            int affectedRows = stmt.executeUpdate();
            
            logger.info("Task status updated with ID: {}, status: {}", taskId, status);
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error updating task status: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean assignVolunteer(int taskId, int volunteerId) throws SQLException {
        String sql = "UPDATE tasks SET volunteer_id = ?, status = 'ASSIGNED' WHERE task_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, volunteerId);
            stmt.setInt(2, taskId);
            
            int affectedRows = stmt.executeUpdate();
            
            logger.info("Volunteer assigned to task with ID: {}, volunteer ID: {}", taskId, volunteerId);
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error assigning volunteer to task: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Map a ResultSet row to a Task object
     * 
     * @param rs the ResultSet
     * @return a Task object
     * @throws SQLException if a database error occurs
     */
    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setTaskId(rs.getInt("task_id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setRequesterId(rs.getInt("requester_id"));
        
        int volunteerId = rs.getInt("volunteer_id");
        if (!rs.wasNull()) {
            task.setVolunteerId(volunteerId);
        }
        
        task.setStatus(rs.getString("status"));
        task.setLocation(rs.getString("location"));
        task.setScheduledDate(rs.getDate("scheduled_date").toLocalDate());
        task.setScheduledTime(rs.getTime("scheduled_time").toLocalTime());
        task.setEstimatedDuration(rs.getInt("estimated_duration"));
        task.setUrgencyLevel(rs.getString("urgency_level"));
        
        int previousVolunteerId = rs.getInt("previous_volunteer_id");
        if (!rs.wasNull()) {
            task.setPreviousVolunteerId(previousVolunteerId);
        }
        
        task.setReassignmentReason(rs.getString("reassignment_reason"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            task.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            task.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return task;
    }
}