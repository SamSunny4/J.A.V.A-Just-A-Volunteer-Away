package com.java.volunteer.service.impl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.volunteer.dao.TaskDao;
import com.java.volunteer.dao.UserDao;
import com.java.volunteer.dao.impl.TaskDaoImpl;
import com.java.volunteer.dao.impl.UserDaoImpl;
import com.java.volunteer.model.Task;
import com.java.volunteer.model.User;
import com.java.volunteer.service.TaskService;

/**
 * Implementation of the TaskService interface
 */
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskDao taskDao;
    private final UserDao userDao;
    
    public TaskServiceImpl() {
        this.taskDao = new TaskDaoImpl();
        this.userDao = new UserDaoImpl();
    }
    
    public TaskServiceImpl(TaskDao taskDao, UserDao userDao) {
        this.taskDao = taskDao;
        this.userDao = userDao;
    }

    @Override
    public Task createTask(String title, String description, int requesterId, String location,
                          LocalDate scheduledDate, LocalTime scheduledTime, int estimatedDuration,
                          String urgencyLevel) throws SQLException, IllegalArgumentException {
        // Validate inputs
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        
        if (requesterId <= 0) {
            throw new IllegalArgumentException("Invalid requester ID");
        }
        
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        
        if (scheduledDate == null) {
            throw new IllegalArgumentException("Scheduled date cannot be null");
        }
        
        if (scheduledDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Scheduled date cannot be in the past");
        }
        
        if (scheduledTime == null) {
            throw new IllegalArgumentException("Scheduled time cannot be null");
        }
        
        if (estimatedDuration <= 0) {
            throw new IllegalArgumentException("Estimated duration must be positive");
        }
        
        // Validate urgency level
        if (urgencyLevel == null || (!urgencyLevel.equals("LOW") && !urgencyLevel.equals("MEDIUM") && !urgencyLevel.equals("HIGH"))) {
            throw new IllegalArgumentException("Invalid urgency level: must be LOW, MEDIUM, or HIGH");
        }
        
        // Check if the requester exists and is an elderly user
        User requester = userDao.findById(requesterId);
        if (requester == null) {
            throw new IllegalArgumentException("Requester not found");
        }
        
        if (!"ELDERLY".equals(requester.getRole())) {
            throw new IllegalArgumentException("Only elderly users can request tasks");
        }
        
        // Create and save the task
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setRequesterId(requesterId);
        task.setLocation(location);
        task.setScheduledDate(scheduledDate);
        task.setScheduledTime(scheduledTime);
        task.setEstimatedDuration(estimatedDuration);
        task.setUrgencyLevel(urgencyLevel);
        task.setStatus("AVAILABLE");
        
        logger.info("Creating new task: {} for requester ID: {}", title, requesterId);
        return taskDao.save(task);
    }

    @Override
    public List<Task> getAllTasks() throws SQLException {
        return taskDao.findAll();
    }

    @Override
    public List<Task> getAvailableTasks() throws SQLException {
        return taskDao.findByStatus("AVAILABLE");
    }

    @Override
    public List<Task> getTasksByStatus(String status) throws SQLException {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        
        return taskDao.findByStatus(status);
    }

    @Override
    public List<Task> getTasksByRequesterId(int requesterId) throws SQLException {
        if (requesterId <= 0) {
            throw new IllegalArgumentException("Invalid requester ID");
        }
        
        return taskDao.findByRequesterId(requesterId);
    }

    @Override
    public List<Task> getTasksByVolunteerId(int volunteerId) throws SQLException {
        if (volunteerId <= 0) {
            throw new IllegalArgumentException("Invalid volunteer ID");
        }
        
        return taskDao.findByVolunteerId(volunteerId);
    }

    @Override
    public Task getTaskById(int taskId) throws SQLException {
        if (taskId <= 0) {
            throw new IllegalArgumentException("Invalid task ID");
        }
        
        return taskDao.findById(taskId);
    }

    @Override
    public Task updateTask(Task task) throws SQLException {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        
        if (task.getTaskId() <= 0) {
            throw new IllegalArgumentException("Invalid task ID");
        }
        
        // Check if the task exists
        Task existingTask = taskDao.findById(task.getTaskId());
        if (existingTask == null) {
            throw new IllegalArgumentException("Task not found");
        }
        
        // Check if the requester exists and is an elderly user
        User requester = userDao.findById(task.getRequesterId());
        if (requester == null) {
            throw new IllegalArgumentException("Requester not found");
        }
        
        if (!"ELDERLY".equals(requester.getRole())) {
            throw new IllegalArgumentException("Only elderly users can request tasks");
        }
        
        // If volunteer ID is provided, check if the volunteer exists and is a volunteer
        if (task.getVolunteerId() != null) {
            User volunteer = userDao.findById(task.getVolunteerId());
            if (volunteer == null) {
                throw new IllegalArgumentException("Volunteer not found");
            }
            
            if (!"VOLUNTEER".equals(volunteer.getRole())) {
                throw new IllegalArgumentException("Only volunteer users can be assigned to tasks");
            }
        }
        
        logger.info("Updating task with ID: {}", task.getTaskId());
        return taskDao.update(task);
    }

    @Override
    public boolean assignTaskToVolunteer(int taskId, int volunteerId) throws SQLException {
        if (taskId <= 0) {
            throw new IllegalArgumentException("Invalid task ID");
        }
        
        if (volunteerId <= 0) {
            throw new IllegalArgumentException("Invalid volunteer ID");
        }
        
        // Check if the task exists
        Task task = taskDao.findById(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found");
        }
        
        // Check if the task is available
        if (!"AVAILABLE".equals(task.getStatus())) {
            throw new IllegalArgumentException("Task is not available");
        }
        
        // Check if the volunteer exists and is a volunteer
        User volunteer = userDao.findById(volunteerId);
        if (volunteer == null) {
            throw new IllegalArgumentException("Volunteer not found");
        }
        
        if (!"VOLUNTEER".equals(volunteer.getRole())) {
            throw new IllegalArgumentException("Only volunteer users can be assigned to tasks");
        }
        
        logger.info("Assigning task ID: {} to volunteer ID: {}", taskId, volunteerId);
        return taskDao.assignVolunteer(taskId, volunteerId);
    }

    @Override
    public boolean updateTaskStatus(int taskId, String status) throws SQLException {
        if (taskId <= 0) {
            throw new IllegalArgumentException("Invalid task ID");
        }
        
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        
        // Validate status
        if (!status.equals("PENDING") && !status.equals("ASSIGNED") && !status.equals("IN_PROGRESS") &&
            !status.equals("COMPLETED") && !status.equals("CANCELLED") && !status.equals("AVAILABLE") &&
            !status.equals("REASSIGNED")) {
            throw new IllegalArgumentException("Invalid status");
        }
        
        // Check if the task exists
        Task task = taskDao.findById(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found");
        }
        
        logger.info("Updating task ID: {} status to: {}", taskId, status);
        return taskDao.updateStatus(taskId, status);
    }

    @Override
    public boolean cancelTask(int taskId) throws SQLException {
        if (taskId <= 0) {
            throw new IllegalArgumentException("Invalid task ID");
        }
        
        // Check if the task exists
        Task task = taskDao.findById(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found");
        }
        
        // Check if the task can be cancelled
        if ("COMPLETED".equals(task.getStatus()) || "CANCELLED".equals(task.getStatus())) {
            throw new IllegalArgumentException("Cannot cancel a completed or already cancelled task");
        }
        
        logger.info("Cancelling task ID: {}", taskId);
        return taskDao.updateStatus(taskId, "CANCELLED");
    }

    @Override
    public boolean deleteTask(int taskId) throws SQLException {
        if (taskId <= 0) {
            throw new IllegalArgumentException("Invalid task ID");
        }
        
        // Check if the task exists
        Task task = taskDao.findById(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found");
        }
        
        logger.info("Deleting task ID: {}", taskId);
        return taskDao.deleteById(taskId);
    }
}