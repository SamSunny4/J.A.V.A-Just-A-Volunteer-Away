package com.java.volunteer.dao;

import java.util.List;

import com.java.volunteer.model.Task;
import com.java.volunteer.model.TaskHistory;

/**
 * DAO interface for TaskHistory entity
 */
public interface TaskHistoryDao extends BaseDao<TaskHistory, Integer> {
    /**
     * Find task history by task ID
     * @param taskId the task ID
     * @return a list of task history entries for the specified task
     */
    List<TaskHistory> findByTaskId(int taskId);
    
    /**
     * Find task history by changed by user ID
     * @param userId the user ID
     * @return a list of task history entries changed by the specified user
     */
    List<TaskHistory> findByChangedById(int userId);
    
    /**
     * Record a task status change
     * @param taskId the task ID
     * @param changedById the user ID making the change
     * @param changedBy the role of the user making the change
     * @param previousStatus the previous task status
     * @param newStatus the new task status
     * @param notes any notes about the change
     * @return the created task history entry
     */
    TaskHistory recordStatusChange(int taskId, int changedById, TaskHistory.ChangedBy changedBy,
                                  Task.Status previousStatus, Task.Status newStatus, 
                                  String notes);
    
    /**
     * Record a task volunteer reassignment
     * @param taskId the task ID
     * @param changedById the user ID making the change
     * @param changedBy the role of the user making the change
     * @param previousStatus the previous task status
     * @param newStatus the new task status
     * @param previousVolunteerId the ID of the previous volunteer (can be null)
     * @param newVolunteerId the ID of the new volunteer (can be null)
     * @param reassignmentReason the reason for reassignment
     * @param notes any notes about the change
     * @return the created task history entry
     */
    TaskHistory recordVolunteerReassignment(int taskId, int changedById, TaskHistory.ChangedBy changedBy,
                                          Task.Status previousStatus, Task.Status newStatus,
                                          Integer previousVolunteerId, Integer newVolunteerId,
                                          String reassignmentReason, String notes);
}