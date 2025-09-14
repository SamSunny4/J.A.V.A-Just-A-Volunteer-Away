package com.java.volunteer.service;

import java.util.List;
import java.util.Optional;

import com.java.volunteer.model.UserPoints;

/**
 * Service interface for user points and leaderboard operations
 */
public interface UserPointsService {
    /**
     * Get user points by user ID
     * @param userId the user ID
     * @return an Optional containing the user points if found, or empty if not found
     */
    Optional<UserPoints> getUserPointsByUserId(int userId);
    
    /**
     * Create user points for a new user
     * @param userId the user ID
     * @return the created user points
     */
    UserPoints createUserPoints(int userId);
    
    /**
     * Get the leaderboard of top volunteers
     * @param limit the maximum number of users to return
     * @return a list of top volunteer user points
     */
    List<UserPoints> getLeaderboard(int limit);
    
    /**
     * Update user points when a task is completed
     * @param userId the user ID
     * @param taskDurationMinutes the duration of the completed task in minutes
     * @return the updated user points
     */
    UserPoints addTaskCompletion(int userId, int taskDurationMinutes);
    
    /**
     * Update user points when a task is reassigned
     * @param userId the user ID
     * @return the updated user points
     */
    UserPoints addTaskReassignment(int userId);
    
    /**
     * Update user's streak
     * @param userId the user ID
     * @return the updated user points
     */
    UserPoints updateStreak(int userId);
    
    /**
     * Update leaderboard positions for all volunteers
     */
    void updateLeaderboardPositions();
}