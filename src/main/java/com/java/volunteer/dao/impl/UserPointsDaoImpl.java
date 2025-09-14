package com.java.volunteer.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.volunteer.config.DatabaseManager;
import com.java.volunteer.dao.UserDao;
import com.java.volunteer.dao.UserPointsDao;
import com.java.volunteer.model.UserPoints;
import com.java.volunteer.model.UserRole;

/**
 * Implementation of UserPointsDao using JDBC
 */
public class UserPointsDaoImpl implements UserPointsDao {
    private static final Logger logger = LoggerFactory.getLogger(UserPointsDaoImpl.class);
    private final UserDao userDao;
    
    public UserPointsDaoImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserPoints create(UserPoints userPoints) {
        String sql = "INSERT INTO user_points (user_id, points, level, rank, " +
                "tasks_completed, tasks_cancelled, tasks_reassigned, hours_volunteered, " +
                "streak_days, last_activity_date, leaderboard_position, badges) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, userPoints.getUserId());
            stmt.setInt(2, userPoints.getPoints());
            stmt.setInt(3, userPoints.getLevel());
            stmt.setString(4, userPoints.getRank());
            stmt.setInt(5, userPoints.getTasksCompleted());
            stmt.setInt(6, userPoints.getTasksCancelled());
            stmt.setInt(7, userPoints.getTasksReassigned());
            stmt.setInt(8, userPoints.getHoursVolunteered());
            stmt.setInt(9, userPoints.getStreakDays());
            
            if (userPoints.getLastActivityDate() != null) {
                stmt.setTimestamp(10, Timestamp.valueOf(userPoints.getLastActivityDate()));
            } else {
                stmt.setNull(10, Types.TIMESTAMP);
            }
            
            if (userPoints.getLeaderboardPosition() != null) {
                stmt.setInt(11, userPoints.getLeaderboardPosition());
            } else {
                stmt.setNull(11, Types.INTEGER);
            }
            
            stmt.setString(12, userPoints.getBadges());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user points failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    userPoints.setPointId(generatedKeys.getInt(1));
                    return userPoints;
                } else {
                    throw new SQLException("Creating user points failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating user points", e);
            throw new RuntimeException("Error creating user points", e);
        }
    }

    @Override
    public Optional<UserPoints> findById(Integer id) {
        String sql = "SELECT * FROM user_points WHERE point_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UserPoints userPoints = mapResultSetToUserPoints(rs);
                    loadUser(userPoints);
                    return Optional.of(userPoints);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user points by ID", e);
            throw new RuntimeException("Error finding user points by ID", e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<UserPoints> findAll() {
        List<UserPoints> userPointsList = new ArrayList<>();
        String sql = "SELECT * FROM user_points";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                UserPoints userPoints = mapResultSetToUserPoints(rs);
                userPointsList.add(userPoints);
            }
            
            // Load users for all user points
            for (UserPoints userPoints : userPointsList) {
                loadUser(userPoints);
            }
        } catch (SQLException e) {
            logger.error("Error finding all user points", e);
            throw new RuntimeException("Error finding all user points", e);
        }
        
        return userPointsList;
    }

    @Override
    public UserPoints update(UserPoints userPoints) {
        String sql = "UPDATE user_points SET points = ?, level = ?, rank = ?, " +
                "tasks_completed = ?, tasks_cancelled = ?, tasks_reassigned = ?, " +
                "hours_volunteered = ?, streak_days = ?, last_activity_date = ?, " +
                "leaderboard_position = ?, badges = ? WHERE point_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userPoints.getPoints());
            stmt.setInt(2, userPoints.getLevel());
            stmt.setString(3, userPoints.getRank());
            stmt.setInt(4, userPoints.getTasksCompleted());
            stmt.setInt(5, userPoints.getTasksCancelled());
            stmt.setInt(6, userPoints.getTasksReassigned());
            stmt.setInt(7, userPoints.getHoursVolunteered());
            stmt.setInt(8, userPoints.getStreakDays());
            
            if (userPoints.getLastActivityDate() != null) {
                stmt.setTimestamp(9, Timestamp.valueOf(userPoints.getLastActivityDate()));
            } else {
                stmt.setNull(9, Types.TIMESTAMP);
            }
            
            if (userPoints.getLeaderboardPosition() != null) {
                stmt.setInt(10, userPoints.getLeaderboardPosition());
            } else {
                stmt.setNull(10, Types.INTEGER);
            }
            
            stmt.setString(11, userPoints.getBadges());
            stmt.setInt(12, userPoints.getPointId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user points failed, no rows affected.");
            }
            
            return userPoints;
        } catch (SQLException e) {
            logger.error("Error updating user points", e);
            throw new RuntimeException("Error updating user points", e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM user_points WHERE point_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error deleting user points", e);
            throw new RuntimeException("Error deleting user points", e);
        }
    }

    @Override
    public Optional<UserPoints> findByUserId(int userId) {
        String sql = "SELECT * FROM user_points WHERE user_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UserPoints userPoints = mapResultSetToUserPoints(rs);
                    loadUser(userPoints);
                    return Optional.of(userPoints);
                } else {
                    // If no points record exists yet, create one for this user
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user points by user ID", e);
            throw new RuntimeException("Error finding user points by user ID", e);
        }
    }

    @Override
    public List<UserPoints> getLeaderboard(int limit) {
        List<UserPoints> leaderboard = new ArrayList<>();
        
        // Only include volunteers (role_id = 3) in the leaderboard
        String sql = "SELECT up.* FROM user_points up " +
                     "JOIN users u ON up.user_id = u.user_id " +
                     "WHERE u.role_id = ? " +
                     "ORDER BY up.points DESC LIMIT ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, UserRole.ROLE_VOLUNTEER);
            stmt.setInt(2, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                int position = 1;
                while (rs.next()) {
                    UserPoints userPoints = mapResultSetToUserPoints(rs);
                    userPoints.setLeaderboardPosition(position++);
                    loadUser(userPoints);
                    leaderboard.add(userPoints);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving leaderboard", e);
            throw new RuntimeException("Error retrieving leaderboard", e);
        }
        
        return leaderboard;
    }

    @Override
    public UserPoints addTaskCompletion(int userId, int taskDurationMinutes) {
        // First try to find existing user points
        Optional<UserPoints> optUserPoints = findByUserId(userId);
        
        UserPoints userPoints;
        if (optUserPoints.isPresent()) {
            userPoints = optUserPoints.get();
        } else {
            // Create new user points record
            userPoints = new UserPoints(userId);
            create(userPoints);
        }
        
        // Update points and stats
        userPoints.completeTask(taskDurationMinutes);
        userPoints.setLastActivityDate(LocalDateTime.now());
        
        // Save changes
        return update(userPoints);
    }

    @Override
    public UserPoints addTaskReassignment(int userId) {
        // First try to find existing user points
        Optional<UserPoints> optUserPoints = findByUserId(userId);
        
        UserPoints userPoints;
        if (optUserPoints.isPresent()) {
            userPoints = optUserPoints.get();
        } else {
            // Create new user points record
            userPoints = new UserPoints(userId);
            create(userPoints);
        }
        
        // Update points and stats
        userPoints.taskReassigned();
        userPoints.setLastActivityDate(LocalDateTime.now());
        
        // Save changes
        return update(userPoints);
    }

    @Override
    public boolean updateLeaderboardPositions() {
        try (Connection conn = DatabaseManager.getConnection()) {
            // First clear all leaderboard positions
            String clearSql = "UPDATE user_points SET leaderboard_position = NULL";
            try (PreparedStatement clearStmt = conn.prepareStatement(clearSql)) {
                clearStmt.executeUpdate();
            }
            
            // Then update positions for volunteers only
            String updateSql = "UPDATE user_points up " +
                               "JOIN (SELECT up2.point_id, " +
                               "      @position := @position + 1 AS position " +
                               "      FROM user_points up2 " +
                               "      JOIN users u ON up2.user_id = u.user_id, " +
                               "      (SELECT @position := 0) AS p " +
                               "      WHERE u.role_id = ? " +
                               "      ORDER BY up2.points DESC) AS ranked " +
                               "ON up.point_id = ranked.point_id " +
                               "SET up.leaderboard_position = ranked.position";
            
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, UserRole.ROLE_VOLUNTEER);
                updateStmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error updating leaderboard positions", e);
            throw new RuntimeException("Error updating leaderboard positions", e);
        }
    }
    
    /**
     * Map a ResultSet to a UserPoints object
     * @param rs the ResultSet
     * @return the UserPoints object
     * @throws SQLException if an error occurs
     */
    private UserPoints mapResultSetToUserPoints(ResultSet rs) throws SQLException {
        UserPoints userPoints = new UserPoints();
        
        userPoints.setPointId(rs.getInt("point_id"));
        userPoints.setUserId(rs.getInt("user_id"));
        userPoints.setPoints(rs.getInt("points"));
        userPoints.setLevel(rs.getInt("level"));
        userPoints.setRank(rs.getString("rank"));
        userPoints.setTasksCompleted(rs.getInt("tasks_completed"));
        userPoints.setTasksCancelled(rs.getInt("tasks_cancelled"));
        userPoints.setTasksReassigned(rs.getInt("tasks_reassigned"));
        userPoints.setHoursVolunteered(rs.getInt("hours_volunteered"));
        userPoints.setStreakDays(rs.getInt("streak_days"));
        
        Timestamp lastActivityDate = rs.getTimestamp("last_activity_date");
        if (lastActivityDate != null) {
            userPoints.setLastActivityDate(lastActivityDate.toLocalDateTime());
        }
        
        int leaderboardPosition = rs.getInt("leaderboard_position");
        if (!rs.wasNull()) {
            userPoints.setLeaderboardPosition(leaderboardPosition);
        }
        
        userPoints.setBadges(rs.getString("badges"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            userPoints.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            userPoints.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return userPoints;
    }
    
    /**
     * Load the user object for a UserPoints object
     * @param userPoints the UserPoints object
     */
    private void loadUser(UserPoints userPoints) {
        userDao.findById(userPoints.getUserId()).ifPresent(userPoints::setUser);
    }
}