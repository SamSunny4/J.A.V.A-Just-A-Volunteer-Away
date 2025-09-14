package com.java.volunteer.dao;

import java.util.List;
import java.util.Optional;

import com.java.volunteer.model.User;

/**
 * DAO interface for User entity
 */
public interface UserDao extends BaseDao<User, Integer> {
    /**
     * Find a user by username
     * @param username the username
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find a user by email
     * @param email the email
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find all users with a specific role
     * @param roleId the role ID
     * @return a list of users with the specified role
     */
    List<User> findByRole(int roleId);
    
    /**
     * Authenticate a user with username and password
     * @param username the username
     * @param password the password (raw, will be hashed inside the method)
     * @return an Optional containing the authenticated user, or empty if authentication failed
     */
    Optional<User> authenticate(String username, String password);
    
    /**
     * Update user's password
     * @param userId the user ID
     * @param newPassword the new password (raw, will be hashed inside the method)
     * @return true if the password was updated, false otherwise
     */
    boolean updatePassword(int userId, String newPassword);
    
    /**
     * Update user's active status
     * @param userId the user ID
     * @param active the new active status
     * @return true if the status was updated, false otherwise
     */
    boolean updateActiveStatus(int userId, boolean active);
}