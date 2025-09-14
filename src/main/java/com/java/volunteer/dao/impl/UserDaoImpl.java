package com.java.volunteer.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.volunteer.config.DatabaseManager;
import com.java.volunteer.dao.UserDao;
import com.java.volunteer.model.User;

/**
 * Implementation of UserDao using JDBC
 */
public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (username, password, email, first_name, last_name, " +
                "phone_number, address, date_of_birth, role_id, is_active, bio, profile_image) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Hash the password before storing
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getPhoneNumber());
            stmt.setString(7, user.getAddress());
            
            // Handle date of birth (might be null)
            if (user.getDateOfBirth() != null) {
                stmt.setDate(8, Date.valueOf(user.getDateOfBirth()));
            } else {
                stmt.setNull(8, Types.DATE);
            }
            
            stmt.setInt(9, user.getRoleId());
            stmt.setBoolean(10, user.isActive());
            stmt.setString(11, user.getBio());
            stmt.setString(12, user.getProfileImage());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                    return user;
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating user", e);
            throw new RuntimeException("Error creating user", e);
        }
    }

    @Override
    public Optional<User> findById(Integer id) {
        String sql = "SELECT u.*, r.role_name FROM users u JOIN user_roles r ON u.role_id = r.role_id WHERE u.user_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user by ID", e);
            throw new RuntimeException("Error finding user by ID", e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name FROM users u JOIN user_roles r ON u.role_id = r.role_id";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding all users", e);
            throw new RuntimeException("Error finding all users", e);
        }
        
        return users;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, first_name = ?, last_name = ?, " +
                "phone_number = ?, address = ?, date_of_birth = ?, role_id = ?, is_active = ?, " +
                "bio = ?, profile_image = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setString(6, user.getAddress());
            
            // Handle date of birth (might be null)
            if (user.getDateOfBirth() != null) {
                stmt.setDate(7, Date.valueOf(user.getDateOfBirth()));
            } else {
                stmt.setNull(7, Types.DATE);
            }
            
            stmt.setInt(8, user.getRoleId());
            stmt.setBoolean(9, user.isActive());
            stmt.setString(10, user.getBio());
            stmt.setString(11, user.getProfileImage());
            stmt.setInt(12, user.getUserId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
            
            return user;
        } catch (SQLException e) {
            logger.error("Error updating user", e);
            throw new RuntimeException("Error updating user", e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error deleting user", e);
            throw new RuntimeException("Error deleting user", e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT u.*, r.role_name FROM users u JOIN user_roles r ON u.role_id = r.role_id WHERE u.username = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user by username", e);
            throw new RuntimeException("Error finding user by username", e);
        }
        
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT u.*, r.role_name FROM users u JOIN user_roles r ON u.role_id = r.role_id WHERE u.email = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user by email", e);
            throw new RuntimeException("Error finding user by email", e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<User> findByRole(int roleId) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name FROM users u JOIN user_roles r ON u.role_id = r.role_id WHERE u.role_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roleId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding users by role", e);
            throw new RuntimeException("Error finding users by role", e);
        }
        
        return users;
    }

    @Override
    public Optional<User> authenticate(String username, String password) {
        String sql = "SELECT u.*, r.role_name FROM users u JOIN user_roles r ON u.role_id = r.role_id WHERE u.username = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Get stored hashed password
                    String storedHash = rs.getString("password");
                    
                    // Check if provided password matches the stored hash
                    if (BCrypt.checkpw(password, storedHash)) {
                        return Optional.of(mapResultSetToUser(rs));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error authenticating user", e);
            throw new RuntimeException("Error authenticating user", e);
        }
        
        return Optional.empty();
    }

    @Override
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Hash the new password
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            
            stmt.setString(1, hashedPassword);
            stmt.setInt(2, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error updating user password", e);
            throw new RuntimeException("Error updating user password", e);
        }
    }

    @Override
    public boolean updateActiveStatus(int userId, boolean active) {
        String sql = "UPDATE users SET is_active = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, active);
            stmt.setInt(2, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error updating user active status", e);
            throw new RuntimeException("Error updating user active status", e);
        }
    }
    
    /**
     * Map a ResultSet to a User object
     * @param rs the ResultSet
     * @return the User object
     * @throws SQLException if an error occurs
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password")); // Note: this is hashed
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setAddress(rs.getString("address"));
        
        Date dateOfBirth = rs.getDate("date_of_birth");
        if (dateOfBirth != null) {
            user.setDateOfBirth(dateOfBirth.toLocalDate());
        }
        
        user.setRoleId(rs.getInt("role_id"));
        user.setRoleName(rs.getString("role_name"));
        user.setActive(rs.getBoolean("is_active"));
        user.setBio(rs.getString("bio"));
        user.setProfileImage(rs.getString("profile_image"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return user;
    }
}