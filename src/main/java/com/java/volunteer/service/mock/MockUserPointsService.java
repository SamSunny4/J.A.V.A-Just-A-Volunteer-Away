package com.java.volunteer.service.mock;

import com.java.volunteer.model.User;
import com.java.volunteer.model.UserPoints;
import com.java.volunteer.service.UserPointsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Mock implementation of UserPointsService for testing and development
 */
public class MockUserPointsService implements UserPointsService {
    private final Map<Integer, UserPoints> userPoints = new HashMap<>();
    private int nextId = 1;
    
    public MockUserPointsService() {
        // Create sample user points
        createSampleUserPoints();
    }
    
    private void createSampleUserPoints() {
        // Points for volunteer user
        UserPoints points1 = new UserPoints(3); // volunteer1
        points1.setPointId(nextId++);
        points1.setPoints(250);
        points1.setLevel(2);
        points1.setRank("Regular Volunteer");
        points1.setTasksCompleted(5);
        points1.setHoursVolunteered(8);
        points1.setStreakDays(3);
        points1.setLeaderboardPosition(1);
        points1.setCreatedAt(LocalDateTime.now().minusDays(30));
        userPoints.put(points1.getUserId(), points1);
        
        // Points for another volunteer (not yet in the system)
        UserPoints points2 = new UserPoints(4); // volunteer2
        points2.setPointId(nextId++);
        points2.setPoints(150);
        points2.setLevel(1);
        points2.setRank("Newcomer");
        points2.setTasksCompleted(3);
        points2.setHoursVolunteered(4);
        points2.setStreakDays(1);
        points2.setLeaderboardPosition(2);
        points2.setCreatedAt(LocalDateTime.now().minusDays(15));
        userPoints.put(points2.getUserId(), points2);
    }

    @Override
    public Optional<UserPoints> getUserPointsByUserId(int userId) {
        return Optional.ofNullable(userPoints.get(userId));
    }

    @Override
    public UserPoints createUserPoints(int userId) {
        UserPoints points = new UserPoints(userId);
        points.setPointId(nextId++);
        points.setCreatedAt(LocalDateTime.now());
        userPoints.put(userId, points);
        updateLeaderboardPositions();
        return points;
    }

    @Override
    public List<UserPoints> getLeaderboard(int limit) {
        List<UserPoints> leaderboard = userPoints.values().stream()
            .sorted(Comparator.comparing(UserPoints::getPoints).reversed())
            .collect(Collectors.toList());
        
        if (limit > 0 && limit < leaderboard.size()) {
            return leaderboard.subList(0, limit);
        }
        return leaderboard;
    }

    @Override
    public UserPoints addTaskCompletion(int userId, int taskDurationMinutes) {
        UserPoints points = userPoints.getOrDefault(userId, createUserPoints(userId));
        points.completeTask(taskDurationMinutes);
        points.setUpdatedAt(LocalDateTime.now());
        updateLeaderboardPositions();
        return points;
    }

    @Override
    public UserPoints addTaskReassignment(int userId) {
        UserPoints points = userPoints.getOrDefault(userId, createUserPoints(userId));
        points.taskReassigned();
        points.setUpdatedAt(LocalDateTime.now());
        updateLeaderboardPositions();
        return points;
    }

    @Override
    public UserPoints updateStreak(int userId) {
        UserPoints points = userPoints.getOrDefault(userId, createUserPoints(userId));
        points.updateStreak();
        points.setUpdatedAt(LocalDateTime.now());
        updateLeaderboardPositions();
        return points;
    }

    @Override
    public void updateLeaderboardPositions() {
        List<UserPoints> sortedPoints = userPoints.values().stream()
            .sorted(Comparator.comparing(UserPoints::getPoints).reversed())
            .collect(Collectors.toList());
        
        for (int i = 0; i < sortedPoints.size(); i++) {
            UserPoints points = sortedPoints.get(i);
            points.setLeaderboardPosition(i + 1);
        }
    }
    
    // Helper method to set User objects for points
    public void setUserForPoints(List<UserPoints> pointsList, Map<Integer, User> users) {
        for (UserPoints points : pointsList) {
            User user = users.get(points.getUserId());
            if (user != null) {
                points.setUser(user);
            }
        }
    }
}