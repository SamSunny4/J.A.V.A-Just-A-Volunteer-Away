package com.java.volunteer.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.java.volunteer.model.User;

/**
 * Service interface for user-related operations
 */
public interface UserService {
    
    /**
     * Register a new user (volunteer or elderly)
     * 
     * @param username the username
     * @param password the password (will be hashed)
     * @param email the email
     * @param firstName the first name
     * @param lastName the last name
     * @param phoneNumber the phone number
     * @param address the address
     * @param dateOfBirth the date of birth
     * @param role the role (VOLUNTEER or ELDERLY)
     * @param bio the bio
     * @return the created user with ID
     * @throws SQLException if a database error occurs
     * @throws IllegalArgumentException if the role is invalid
     */
    User registerUser(String username, String password, String email, String firstName, 
                     String lastName, String phoneNumber, String address, 
                     LocalDate dateOfBirth, String role, String bio) throws SQLException, IllegalArgumentException;
    
    /**
     * Authenticate a user with username and password
     * 
     * @param username the username
     * @param password the password
     * @return the authenticated user or null if authentication fails
     * @throws SQLException if a database error occurs
     */
    User authenticateUser(String username, String password) throws SQLException;
    
    /**
     * Get all users
     * 
     * @return a list of all users
     * @throws SQLException if a database error occurs
     */
    List<User> getAllUsers() throws SQLException;
    
    /**
     * Get all volunteers
     * 
     * @return a list of all volunteer users
     * @throws SQLException if a database error occurs
     */
    List<User> getAllVolunteers() throws SQLException;
    
    /**
     * Get all elderly users
     * 
     * @return a list of all elderly users
     * @throws SQLException if a database error occurs
     */
    List<User> getAllElderlyUsers() throws SQLException;
    
    /**
     * Get a user by ID
     * 
     * @param userId the user ID
     * @return the user or null if not found
     * @throws SQLException if a database error occurs
     */
    User getUserById(int userId) throws SQLException;
    
    /**
     * Update a user's profile
     * 
     * @param user the user with updated information
     * @return the updated user
     * @throws SQLException if a database error occurs
     */
    User updateUserProfile(User user) throws SQLException;
    
    /**
     * Change a user's active status
     * 
     * @param userId the user ID
     * @param active the new active status
     * @return true if the status was changed, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean changeUserActiveStatus(int userId, boolean active) throws SQLException;
}