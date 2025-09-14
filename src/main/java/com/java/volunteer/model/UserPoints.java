package com.java.volunteer.model;

import java.time.LocalDateTime;

/**
 * Represents user points for gamification and leaderboard
 */
public class UserPoints {
    private int pointId;
    private int userId;
    private int points;
    private int level;
    private String rank;
    private int tasksCompleted;
    private int tasksCancelled;
    private int tasksReassigned;
    private int hoursVolunteered;
    private int streakDays;
    private LocalDateTime lastActivityDate;
    private Integer leaderboardPosition; // Can be null if not ranked yet
    private String badges;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // User reference for convenience (not stored in DB)
    private User user;

    public UserPoints() {
    }

    public UserPoints(int userId) {
        this.userId = userId;
        this.points = 0;
        this.level = 1;
        this.rank = "Newcomer";
        this.tasksCompleted = 0;
        this.tasksCancelled = 0;
        this.tasksReassigned = 0;
        this.hoursVolunteered = 0;
        this.streakDays = 0;
    }

    // Getters and Setters
    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getTasksCompleted() {
        return tasksCompleted;
    }

    public void setTasksCompleted(int tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    public int getTasksCancelled() {
        return tasksCancelled;
    }

    public void setTasksCancelled(int tasksCancelled) {
        this.tasksCancelled = tasksCancelled;
    }

    public int getTasksReassigned() {
        return tasksReassigned;
    }

    public void setTasksReassigned(int tasksReassigned) {
        this.tasksReassigned = tasksReassigned;
    }

    public int getHoursVolunteered() {
        return hoursVolunteered;
    }

    public void setHoursVolunteered(int hoursVolunteered) {
        this.hoursVolunteered = hoursVolunteered;
    }

    public int getStreakDays() {
        return streakDays;
    }

    public void setStreakDays(int streakDays) {
        this.streakDays = streakDays;
    }

    public LocalDateTime getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(LocalDateTime lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public Integer getLeaderboardPosition() {
        return leaderboardPosition;
    }

    public void setLeaderboardPosition(Integer leaderboardPosition) {
        this.leaderboardPosition = leaderboardPosition;
    }

    public String getBadges() {
        return badges;
    }

    public void setBadges(String badges) {
        this.badges = badges;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    // Utility methods for point management
    public void addPoints(int points) {
        this.points += points;
        updateLevelAndRank();
    }
    
    public void completeTask(int durationMinutes) {
        this.tasksCompleted++;
        this.hoursVolunteered += (durationMinutes / 60);
        this.addPoints(50); // Award 50 points for completing a task
    }
    
    public void updateStreak() {
        this.streakDays++;
        if (streakDays % 7 == 0) {
            // Bonus points for maintaining a streak
            this.addPoints(100);
        }
    }
    
    public void taskReassigned() {
        this.tasksReassigned++;
        this.addPoints(-20); // Penalty for having task reassigned
    }
    
    private void updateLevelAndRank() {
        // Calculate level based on points (simple algorithm)
        this.level = 1 + (this.points / 500);
        
        // Update rank based on level
        if (level >= 10) {
            this.rank = "Master Volunteer";
        } else if (level >= 7) {
            this.rank = "Expert Volunteer";
        } else if (level >= 5) {
            this.rank = "Senior Volunteer";
        } else if (level >= 3) {
            this.rank = "Regular Volunteer";
        } else {
            this.rank = "Newcomer";
        }
    }

    @Override
    public String toString() {
        String userName = (user != null) ? user.getUsername() : "Unknown";
        return "UserPoints{" +
                "user='" + userName + '\'' +
                ", points=" + points +
                ", level=" + level +
                ", rank='" + rank + '\'' +
                ", tasksCompleted=" + tasksCompleted +
                ", hoursVolunteered=" + hoursVolunteered +
                ", streakDays=" + streakDays +
                ", leaderboardPosition=" + leaderboardPosition +
                '}';
    }
}