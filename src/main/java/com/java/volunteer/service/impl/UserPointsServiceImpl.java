package com.java.volunteer.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.volunteer.dao.UserDao;
import com.java.volunteer.dao.UserPointsDao;
import com.java.volunteer.model.User;
import com.java.volunteer.model.UserPoints;
import com.java.volunteer.service.UserPointsService;

/**
 * Implementation of UserPointsService
 */
public class UserPointsServiceImpl implements UserPointsService {
    private static final Logger logger = LoggerFactory.getLogger(UserPointsServiceImpl.class);
    private final UserPointsDao userPointsDao;
    private final UserDao userDao;
    
    public UserPointsServiceImpl(UserPointsDao userPointsDao, UserDao userDao) {
        this.userPointsDao = userPointsDao;
        this.userDao = userDao;
    }

    @Override
    public Optional<UserPoints> getUserPointsByUserId(int userId) {
        return userPointsDao.findByUserId(userId);
    }

    @Override
    public UserPoints createUserPoints(int userId) {
        // Make sure the user exists
        Optional<User> user = userDao.findById(userId);
        if (!user.isPresent()) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        
        // Check if user points already exist
        Optional<UserPoints> existingPoints = userPointsDao.findByUserId(userId);
        if (existingPoints.isPresent()) {
            return existingPoints.get();
        }
        
        // Create new user points
        UserPoints userPoints = new UserPoints(userId);
        userPoints.setLastActivityDate(LocalDateTime.now());
        
        logger.info("Creating user points for user: {}", userId);
        return userPointsDao.create(userPoints);
    }

    @Override
    public List<UserPoints> getLeaderboard(int limit) {
        // Update leaderboard positions first
        updateLeaderboardPositions();
        
        logger.info("Retrieving leaderboard with limit: {}", limit);
        return userPointsDao.getLeaderboard(limit);
    }

    @Override
    public UserPoints addTaskCompletion(int userId, int taskDurationMinutes) {
        // Make sure the user exists
        Optional<User> user = userDao.findById(userId);
        if (!user.isPresent()) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        
        logger.info("Adding task completion for user: {}", userId);
        return userPointsDao.addTaskCompletion(userId, taskDurationMinutes);
    }

    @Override
    public UserPoints addTaskReassignment(int userId) {
        // Make sure the user exists
        Optional<User> user = userDao.findById(userId);
        if (!user.isPresent()) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        
        logger.info("Adding task reassignment for user: {}", userId);
        return userPointsDao.addTaskReassignment(userId);
    }

    @Override
    public UserPoints updateStreak(int userId) {
        // Get user points
        Optional<UserPoints> optUserPoints = userPointsDao.findByUserId(userId);
        
        UserPoints userPoints;
        if (optUserPoints.isPresent()) {
            userPoints = optUserPoints.get();
        } else {
            userPoints = createUserPoints(userId);
        }
        
        // Update streak
        userPoints.updateStreak();
        userPoints.setLastActivityDate(LocalDateTime.now());
        
        logger.info("Updating streak for user: {}", userId);
        return userPointsDao.update(userPoints);
    }

    @Override
    public void updateLeaderboardPositions() {
        logger.info("Updating leaderboard positions");
        userPointsDao.updateLeaderboardPositions();
    }
}