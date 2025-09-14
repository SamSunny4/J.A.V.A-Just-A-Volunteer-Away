package com.java.volunteer.dao;

import java.time.LocalDate;
import java.util.List;

import com.java.volunteer.model.Task;

/**
 * DAO interface for Task entity
 */
public interface TaskDao extends BaseDao<Task, Integer> {
    /**
     * Find tasks by requester ID
     * @param requesterId the requester user ID
     * @return a list of tasks requested by the user
     */
    List<Task> findByRequesterId(int requesterId);
    
    /**
     * Find tasks by volunteer ID
     * @param volunteerId the volunteer user ID
     * @return a list of tasks assigned to the volunteer
     */
    List<Task> findByVolunteerId(int volunteerId);
    
    /**
     * Find tasks by status
     * @param status the task status
     * @return a list of tasks with the specified status
     */
    List<Task> findByStatus(Task.Status status);
    
    /**
     * Find available tasks (status = AVAILABLE)
     * @return a list of available tasks
     */
    List<Task> findAvailableTasks();
    
    /**
     * Find tasks scheduled for a specific date
     * @param date the scheduled date
     * @return a list of tasks scheduled for the specified date
     */
    List<Task> findByScheduledDate(LocalDate date);
    
    /**
     * Assign a task to a volunteer
     * @param taskId the task ID
     * @param volunteerId the volunteer user ID
     * @return true if the task was assigned, false otherwise
     */
    boolean assignTask(int taskId, int volunteerId);
    
    /**
     * Update task status
     * @param taskId the task ID
     * @param status the new status
     * @return true if the status was updated, false otherwise
     */
    boolean updateStatus(int taskId, Task.Status status);
    
    /**
     * Reassign a task (remove volunteer)
     * @param taskId the task ID
     * @param reason the reason for reassignment
     * @return true if the task was reassigned, false otherwise
     */
    boolean reassignTask(int taskId, String reason);
    
    /**
     * Count tasks by status
     * @param status the task status
     * @return the number of tasks with the specified status
     */
    int countByStatus(Task.Status status);
}