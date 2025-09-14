package com.java.volunteer.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.java.volunteer.model.Task;
import com.java.volunteer.model.TaskHistory;

/**
 * Service interface for task-related operations
 */
public interface TaskService {
    /**
     * Create a new task
     * @param task the task to create
     * @return the created task
     */
    Task createTask(Task task);
    
    /**
     * Get a task by ID
     * @param taskId the task ID
     * @return an Optional containing the task if found, or empty if not found
     */
    Optional<Task> getTaskById(int taskId);
    
    /**
     * Get all tasks
     * @return a list of all tasks
     */
    List<Task> getAllTasks();
    
    /**
     * Get tasks by requester ID
     * @param requesterId the requester user ID
     * @return a list of tasks requested by the user
     */
    List<Task> getTasksByRequesterId(int requesterId);
    
    /**
     * Get tasks by volunteer ID
     * @param volunteerId the volunteer user ID
     * @return a list of tasks assigned to the volunteer
     */
    List<Task> getTasksByVolunteerId(int volunteerId);
    
    /**
     * Get tasks by status
     * @param status the task status
     * @return a list of tasks with the specified status
     */
    List<Task> getTasksByStatus(Task.Status status);
    
    /**
     * Get available tasks that can be assigned to volunteers
     * @return a list of available tasks
     */
    List<Task> getAvailableTasks();
    
    /**
     * Get tasks scheduled for a specific date
     * @param date the scheduled date
     * @return a list of tasks scheduled for the specified date
     */
    List<Task> getTasksByScheduledDate(LocalDate date);
    
    /**
     * Assign a task to a volunteer
     * @param taskId the task ID
     * @param volunteerId the volunteer user ID
     * @param changedById the ID of the user making the change
     * @param changedBy the role of the user making the change
     * @return the updated task
     */
    Task assignTask(int taskId, int volunteerId, int changedById, TaskHistory.ChangedBy changedBy);
    
    /**
     * Start a task (change status to IN_PROGRESS)
     * @param taskId the task ID
     * @param volunteerId the volunteer user ID
     * @return the updated task
     */
    Task startTask(int taskId, int volunteerId);
    
    /**
     * Complete a task
     * @param taskId the task ID
     * @param volunteerId the volunteer user ID
     * @return the updated task
     */
    Task completeTask(int taskId, int volunteerId);
    
    /**
     * Cancel a task
     * @param taskId the task ID
     * @param userId the user ID cancelling the task
     * @param changedBy the role of the user cancelling the task
     * @param reason the reason for cancellation
     * @return the updated task
     */
    Task cancelTask(int taskId, int userId, TaskHistory.ChangedBy changedBy, String reason);
    
    /**
     * Reassign a task (remove volunteer and make available again)
     * @param taskId the task ID
     * @param requesterId the requester user ID
     * @param reason the reason for reassignment
     * @return the updated task
     */
    Task reassignTask(int taskId, int requesterId, String reason);
    
    /**
     * Update a task
     * @param task the task to update
     * @return the updated task
     */
    Task updateTask(Task task);
    
    /**
     * Delete a task
     * @param taskId the task ID
     * @return true if the task was deleted, false otherwise
     */
    boolean deleteTask(int taskId);
    
    /**
     * Get task history
     * @param taskId the task ID
     * @return a list of task history entries
     */
    List<TaskHistory> getTaskHistory(int taskId);
}