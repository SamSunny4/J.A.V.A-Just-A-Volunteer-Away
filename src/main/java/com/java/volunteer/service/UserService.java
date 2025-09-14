package com.java.volunteer.service;

import java.util.List;
import java.util.Optional;

import com.java.volunteer.model.User;

/**
 * Service interface for user-related operations
 */
public interface UserService {
    /**
     * Register a new user
     * @param user the user to register
     * @return the registered user
     */
    User register(User user);
    
    /**
     * Authenticate a user
     * @param username the username
     * @param password the password
     * @return an Optional containing the authenticated user, or empty if authentication failed
     */
    Optional<User> authenticate(String username, String password);
    
    /**
     * Get a user by ID
     * @param userId the user ID
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> getUserById(int userId);
    
    /**
     * Get all users
     * @return a list of all users
     */
    List<User> getAllUsers();
    
    /**
     * Get users by role
     * @param roleId the role ID
     * @return a list of users with the specified role
     */
    List<User> getUsersByRole(int roleId);
    
    /**
     * Update a user's profile
     * @param user the user to update
     * @return the updated user
     */
    User updateProfile(User user);
    
    /**
     * Change a user's password
     * @param userId the user ID
     * @param oldPassword the old password
     * @param newPassword the new password
     * @return true if the password was changed, false otherwise
     */
    boolean changePassword(int userId, String oldPassword, String newPassword);
    
    /**
     * Deactivate a user
     * @param userId the user ID
     * @return true if the user was deactivated, false otherwise
     */
    boolean deactivateUser(int userId);
    
    /**
     * Activate a user
     * @param userId the user ID
     * @return true if the user was activated, false otherwise
     */
    boolean activateUser(int userId);
}