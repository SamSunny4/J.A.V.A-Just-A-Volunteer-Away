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
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.volunteer.config.DatabaseManager;
import com.java.volunteer.dao.TaskDao;
import com.java.volunteer.dao.UserDao;
import com.java.volunteer.model.Task;

/**
 * Implementation of TaskDao using JDBC
 */
public class TaskDaoImpl implements TaskDao {
    private static final Logger logger = LoggerFactory.getLogger(TaskDaoImpl.class);
    private final UserDao userDao;
    
    public TaskDaoImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Task create(Task task) {
        String sql = "INSERT INTO tasks (title, description, requester_id, volunteer_id, status, " +
                "location, scheduled_date, scheduled_time, estimated_duration, urgency_level, " +
                "previous_volunteer_id, reassignment_reason) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setInt(3, task.getRequesterId());
            
            if (task.getVolunteerId() != null) {
                stmt.setInt(4, task.getVolunteerId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setString(5, task.getStatus().name());
            stmt.setString(6, task.getLocation());
            stmt.setDate(7, Date.valueOf(task.getScheduledDate()));
            stmt.setTime(8, Time.valueOf(task.getScheduledTime()));
            stmt.setInt(9, task.getEstimatedDuration());
            stmt.setString(10, task.getUrgencyLevel().name());
            
            if (task.getPreviousVolunteerId() != null) {
                stmt.setInt(11, task.getPreviousVolunteerId());
            } else {
                stmt.setNull(11, Types.INTEGER);
            }
            
            stmt.setString(12, task.getReassignmentReason());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating task failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    task.setTaskId(generatedKeys.getInt(1));
                    return task;
                } else {
                    throw new SQLException("Creating task failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating task", e);
            throw new RuntimeException("Error creating task", e);
        }
    }

    @Override
    public Optional<Task> findById(Integer id) {
        String sql = "SELECT * FROM tasks WHERE task_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Task task = mapResultSetToTask(rs);
                    loadTaskUsers(task);
                    return Optional.of(task);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding task by ID", e);
            throw new RuntimeException("Error finding task by ID", e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Task task = mapResultSetToTask(rs);
                tasks.add(task);
            }
            
            // Load users for all tasks
            for (Task task : tasks) {
                loadTaskUsers(task);
            }
        } catch (SQLException e) {
            logger.error("Error finding all tasks", e);
            throw new RuntimeException("Error finding all tasks", e);
        }
        
        return tasks;
    }

    @Override
    public Task update(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, requester_id = ?, volunteer_id = ?, " +
                "status = ?, location = ?, scheduled_date = ?, scheduled_time = ?, " +
                "estimated_duration = ?, urgency_level = ?, previous_volunteer_id = ?, " +
                "reassignment_reason = ? WHERE task_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setInt(3, task.getRequesterId());
            
            if (task.getVolunteerId() != null) {
                stmt.setInt(4, task.getVolunteerId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setString(5, task.getStatus().name());
            stmt.setString(6, task.getLocation());
            stmt.setDate(7, Date.valueOf(task.getScheduledDate()));
            stmt.setTime(8, Time.valueOf(task.getScheduledTime()));
            stmt.setInt(9, task.getEstimatedDuration());
            stmt.setString(10, task.getUrgencyLevel().name());
            
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
            
            return task;
        } catch (SQLException e) {
            logger.error("Error updating task", e);
            throw new RuntimeException("Error updating task", e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM tasks WHERE task_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error deleting task", e);
            throw new RuntimeException("Error deleting task", e);
        }
    }

    @Override
    public List<Task> findByRequesterId(int requesterId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE requester_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, requesterId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Task task = mapResultSetToTask(rs);
                    loadTaskUsers(task);
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding tasks by requester ID", e);
            throw new RuntimeException("Error finding tasks by requester ID", e);
        }
        
        return tasks;
    }

    @Override
    public List<Task> findByVolunteerId(int volunteerId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE volunteer_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, volunteerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Task task = mapResultSetToTask(rs);
                    loadTaskUsers(task);
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding tasks by volunteer ID", e);
            throw new RuntimeException("Error finding tasks by volunteer ID", e);
        }
        
        return tasks;
    }

    @Override
    public List<Task> findByStatus(Task.Status status) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE status = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Task task = mapResultSetToTask(rs);
                    loadTaskUsers(task);
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding tasks by status", e);
            throw new RuntimeException("Error finding tasks by status", e);
        }
        
        return tasks;
    }

    @Override
    public List<Task> findAvailableTasks() {
        return findByStatus(Task.Status.AVAILABLE);
    }

    @Override
    public List<Task> findByScheduledDate(LocalDate date) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE scheduled_date = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Task task = mapResultSetToTask(rs);
                    loadTaskUsers(task);
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding tasks by scheduled date", e);
            throw new RuntimeException("Error finding tasks by scheduled date", e);
        }
        
        return tasks;
    }

    @Override
    public boolean assignTask(int taskId, int volunteerId) {
        // First, check if the task is available
        Optional<Task> optTask = findById(taskId);
        if (!optTask.isPresent() || optTask.get().getStatus() != Task.Status.AVAILABLE) {
            return false;
        }
        
        String sql = "UPDATE tasks SET volunteer_id = ?, status = ? WHERE task_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, volunteerId);
            stmt.setString(2, Task.Status.ASSIGNED.name());
            stmt.setInt(3, taskId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error assigning task", e);
            throw new RuntimeException("Error assigning task", e);
        }
    }

    @Override
    public boolean updateStatus(int taskId, Task.Status status) {
        String sql = "UPDATE tasks SET status = ? WHERE task_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            stmt.setInt(2, taskId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error updating task status", e);
            throw new RuntimeException("Error updating task status", e);
        }
    }

    @Override
    public boolean reassignTask(int taskId, String reason) {
        // First, check if the task is assigned or in progress
        Optional<Task> optTask = findById(taskId);
        if (!optTask.isPresent() || optTask.get().getVolunteerId() == null) {
            return false;
        }
        
        Task task = optTask.get();
        
        String sql = "UPDATE tasks SET previous_volunteer_id = volunteer_id, volunteer_id = NULL, " +
                "status = ?, reassignment_reason = ? WHERE task_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, Task.Status.AVAILABLE.name());
            stmt.setString(2, reason);
            stmt.setInt(3, taskId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error reassigning task", e);
            throw new RuntimeException("Error reassigning task", e);
        }
    }

    @Override
    public int countByStatus(Task.Status status) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE status = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.error("Error counting tasks by status", e);
            throw new RuntimeException("Error counting tasks by status", e);
        }
        
        return 0;
    }
    
    /**
     * Map a ResultSet to a Task object
     * @param rs the ResultSet
     * @return the Task object
     * @throws SQLException if an error occurs
     */
    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        
        task.setTaskId(rs.getInt("task_id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setRequesterId(rs.getInt("requester_id"));
        
        Integer volunteerId = rs.getInt("volunteer_id");
        if (rs.wasNull()) {
            task.setVolunteerId(null);
        } else {
            task.setVolunteerId(volunteerId);
        }
        
        task.setStatus(Task.Status.valueOf(rs.getString("status")));
        task.setLocation(rs.getString("location"));
        task.setScheduledDate(rs.getDate("scheduled_date").toLocalDate());
        task.setScheduledTime(rs.getTime("scheduled_time").toLocalTime());
        task.setEstimatedDuration(rs.getInt("estimated_duration"));
        task.setUrgencyLevel(Task.UrgencyLevel.valueOf(rs.getString("urgency_level")));
        
        Integer previousVolunteerId = rs.getInt("previous_volunteer_id");
        if (rs.wasNull()) {
            task.setPreviousVolunteerId(null);
        } else {
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
    
    /**
     * Load user objects related to a task (requester, volunteer, previous volunteer)
     * @param task the task to load users for
     */
    private void loadTaskUsers(Task task) {
        // Load requester
        userDao.findById(task.getRequesterId()).ifPresent(task::setRequester);
        
        // Load volunteer (if assigned)
        if (task.getVolunteerId() != null) {
            userDao.findById(task.getVolunteerId()).ifPresent(task::setVolunteer);
        }
        
        // Load previous volunteer (if reassigned)
        if (task.getPreviousVolunteerId() != null) {
            userDao.findById(task.getPreviousVolunteerId()).ifPresent(task::setPreviousVolunteer);
        }
    }
}