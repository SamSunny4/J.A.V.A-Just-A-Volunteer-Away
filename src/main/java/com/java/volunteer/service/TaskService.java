package com.java.volunteer.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.java.volunteer.model.Task;

/**
 * Service interface for task-related operations
 */
public interface TaskService {
    
    /**
     * Create a new task
     * 
     * @param title the task title
     * @param description the task description
     * @param requesterId the ID of the elderly user who requested the task
     * @param location the task location
     * @param scheduledDate the scheduled date
     * @param scheduledTime the scheduled time
     * @param estimatedDuration the estimated duration in minutes
     * @param urgencyLevel the urgency level (LOW, MEDIUM, HIGH)
     * @return the created task with ID
     * @throws SQLException if a database error occurs
     * @throws IllegalArgumentException if any parameter is invalid
     */
    Task createTask(String title, String description, int requesterId, String location,
                   LocalDate scheduledDate, LocalTime scheduledTime, int estimatedDuration,
                   String urgencyLevel) throws SQLException, IllegalArgumentException;
    
    /**
     * Get all tasks
     * 
     * @return a list of all tasks
     * @throws SQLException if a database error occurs
     */
    List<Task> getAllTasks() throws SQLException;
    
    /**
     * Get available tasks (not assigned to any volunteer)
     * 
     * @return a list of available tasks
     * @throws SQLException if a database error occurs
     */
    List<Task> getAvailableTasks() throws SQLException;
    
    /**
     * Get tasks by status
     * 
     * @param status the task status
     * @return a list of tasks with the specified status
     * @throws SQLException if a database error occurs
     */
    List<Task> getTasksByStatus(String status) throws SQLException;
    
    /**
     * Get tasks requested by an elderly user
     * 
     * @param requesterId the elderly user ID
     * @return a list of tasks requested by the specified user
     * @throws SQLException if a database error occurs
     */
    List<Task> getTasksByRequesterId(int requesterId) throws SQLException;
    
    /**
     * Get tasks assigned to a volunteer
     * 
     * @param volunteerId the volunteer ID
     * @return a list of tasks assigned to the specified volunteer
     * @throws SQLException if a database error occurs
     */
    List<Task> getTasksByVolunteerId(int volunteerId) throws SQLException;
    
    /**
     * Get a task by ID
     * 
     * @param taskId the task ID
     * @return the task or null if not found
     * @throws SQLException if a database error occurs
     */
    Task getTaskById(int taskId) throws SQLException;
    
    /**
     * Update a task
     * 
     * @param task the task with updated information
     * @return the updated task
     * @throws SQLException if a database error occurs
     */
    Task updateTask(Task task) throws SQLException;
    
    /**
     * Assign a task to a volunteer
     * 
     * @param taskId the task ID
     * @param volunteerId the volunteer ID
     * @return true if the task was assigned, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean assignTaskToVolunteer(int taskId, int volunteerId) throws SQLException;
    
    /**
     * Update the status of a task
     * 
     * @param taskId the task ID
     * @param status the new status
     * @return true if the status was updated, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean updateTaskStatus(int taskId, String status) throws SQLException;
    
    /**
     * Cancel a task
     * 
     * @param taskId the task ID
     * @return true if the task was cancelled, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean cancelTask(int taskId) throws SQLException;
    
    /**
     * Delete a task
     * 
     * @param taskId the task ID
     * @return true if the task was deleted, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean deleteTask(int taskId) throws SQLException;
}