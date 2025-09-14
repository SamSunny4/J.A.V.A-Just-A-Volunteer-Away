package com.java.volunteer.dao;

import java.util.List;
import java.util.Optional;

import com.java.volunteer.model.UserPoints;

/**
 * DAO interface for UserPoints entity
 */
public interface UserPointsDao extends BaseDao<UserPoints, Integer> {
    /**
     * Find user points by user ID
     * @param userId the user ID
     * @return an Optional containing the user points if found, or empty if not found
     */
    Optional<UserPoints> findByUserId(int userId);
    
    /**
     * Get the top volunteers by points for the leaderboard
     * @param limit the maximum number of users to return
     * @return a list of top volunteer user points
     */
    List<UserPoints> getLeaderboard(int limit);
    
    /**
     * Update user points when a task is completed
     * @param userId the user ID
     * @param taskDurationMinutes the duration of the completed task in minutes
     * @return the updated UserPoints
     */
    UserPoints addTaskCompletion(int userId, int taskDurationMinutes);
    
    /**
     * Update user points when a task is reassigned
     * @param userId the user ID
     * @return the updated UserPoints
     */
    UserPoints addTaskReassignment(int userId);
    
    /**
     * Update leaderboard positions for all volunteers
     * @return true if the update was successful, false otherwise
     */
    boolean updateLeaderboardPositions();
}