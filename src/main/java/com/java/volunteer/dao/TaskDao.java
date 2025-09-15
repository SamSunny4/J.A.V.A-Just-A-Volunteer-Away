package com.java.volunteer.dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.java.volunteer.model.Task;

/**
 * Task DAO interface with operations specific to tasks
 */
public interface TaskDao extends BaseDao<Task, Integer> {
    
    /**
     * Find tasks by requester ID
     * 
     * @param requesterId the ID of the elderly user who requested the tasks
     * @return a list of tasks requested by the specified user
     * @throws SQLException if a database error occurs
     */
    List<Task> findByRequesterId(int requesterId) throws SQLException;
    
    /**
     * Find tasks by volunteer ID
     * 
     * @param volunteerId the ID of the volunteer assigned to the tasks
     * @return a list of tasks assigned to the specified volunteer
     * @throws SQLException if a database error occurs
     */
    List<Task> findByVolunteerId(int volunteerId) throws SQLException;
    
    /**
     * Find tasks by status
     * 
     * @param status the task status
     * @return a list of tasks with the specified status
     * @throws SQLException if a database error occurs
     */
    List<Task> findByStatus(String status) throws SQLException;
    
    /**
     * Find tasks by scheduled date
     * 
     * @param date the scheduled date
     * @return a list of tasks scheduled on the specified date
     * @throws SQLException if a database error occurs
     */
    List<Task> findByScheduledDate(LocalDate date) throws SQLException;
    
    /**
     * Update the status of a task
     * 
     * @param taskId the task ID
     * @param status the new status
     * @return true if the status was updated, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean updateStatus(int taskId, String status) throws SQLException;
    
    /**
     * Assign a volunteer to a task
     * 
     * @param taskId the task ID
     * @param volunteerId the volunteer ID
     * @return true if the volunteer was assigned, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean assignVolunteer(int taskId, int volunteerId) throws SQLException;
}